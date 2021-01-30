--------------------------------------------------------------------------------------------------------------
-- get factory workers
CREATE OR REPLACE FUNCTION get_Workers(factory_id integer)
    RETURNS TABLE
            (
                name       varchar(32),
                surname    varchar(32),
                birth_date timestamp,
                contacts   varchar(32),
                address    text,
                salary     double precision
            )
AS
$$
SELECT humans.name, humans.surname, humans.birth_date, humans.contacts, humans.address, providers.salary
FROM humans
         JOIN providers ON providers.human_id = humans.id
WHERE providers.factory_id = $1;
$$ LANGUAGE sql;
--------------------------------------------------------------------------------------------------------------


--------------------------------------------------------------------------------------------------------------
-- get clients for provider
CREATE OR REPLACE FUNCTION get_clients_for_provider(provider_id integer)
    RETURNS TABLE
            (
                name    varchar(32),
                surname varchar(32)
            )
AS
$$
SELECT humans.name, humans.surname
FROM humans
WHERE (humans.id IN (SELECT clients.human_id
                     FROM clients
                     WHERE (clients.delivery_place_id =
                            (SELECT p.delivery_place_id FROM providers AS p WHERE (p.id = $1)))));
$$ LANGUAGE sql;
---------------------------------------------------------------------------------------------------------------


--------------------------------------------------------------------------------------------------------------
-- get clients for factories
CREATE OR REPLACE FUNCTION get_clients_for_factory(factory_id integer)
    RETURNS TABLE
            (
                name    varchar(32),
                surname varchar(32)
            )
AS
$$
SELECT humans.name, humans.surname
FROM humans
WHERE (humans.id IN (SELECT clients.human_id
                     FROM clients
                     WHERE (clients.delivery_place_id IN (SELECT providers.delivery_place_id
                                                          FROM providers
                                                          WHERE (providers.factory_id = $1)))));
$$
    LANGUAGE sql;
---------------------------------------------------------------------------------------------------------------












--------------------------------------------------------------------------------------------------------------
-- increment client number delivery_place
CREATE OR REPLACE FUNCTION inc_DelClientNum() RETURNS TRIGGER AS
$$
BEGIN
    UPDATE delivery_places
    SET client_num = client_num + 1
    WHERE delivery_places.id = NEW.delivery_place_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER inc_DelClientNum
    AFTER INSERT
    ON clients
    FOR EACH ROW
EXECUTE PROCEDURE inc_DelClientNum();
--------------------------------------------------------------------------------------------------------------


--------------------------------------------------------------------------------------------------------------
-- decrement client number delivery_place
CREATE OR REPLACE FUNCTION dec_DelClientNum() RETURNS TRIGGER AS
$$
BEGIN
    UPDATE delivery_places
    SET client_num = client_num - 1
    WHERE delivery_places.id = OLD.delivery_place_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER dec_DelClientNum
    AFTER DELETE
    ON clients
    FOR EACH ROW
EXECUTE PROCEDURE dec_DelClientNum();
--------------------------------------------------------------------------------------------------------------


--------------------------------------------------------------------------------------------------------------
-- increment worker number
CREATE OR REPLACE FUNCTION inc_WorkerNum() RETURNS TRIGGER AS
$$
BEGIN
    UPDATE factories
    SET worker_num = worker_num + 1
    WHERE factories.id = NEW.factory_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER inc_WorkerNum
    AFTER INSERT
    ON providers
    FOR EACH ROW
EXECUTE PROCEDURE inc_WorkerNum();
--------------------------------------------------------------------------------------------------------------


--------------------------------------------------------------------------------------------------------------
-- decrement worker number
CREATE OR REPLACE FUNCTION dec_WorkerNum() RETURNS TRIGGER AS
$$
BEGIN
    UPDATE factories
    SET worker_num = worker_num - 1
    WHERE factories.id = OLD.factory_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER dec_WorkerNum
    AFTER DELETE
    ON providers
    FOR EACH ROW
EXECUTE PROCEDURE dec_WorkerNum();
--------------------------------------------------------------------------------------------------------------


--------------------------------------------------------------------------------------------------------------
-- schedule
CREATE OR REPLACE FUNCTION make_Schedule() RETURNS TRIGGER AS
$$
DECLARE
    del_place integer;
    provider  integer;
    weight    integer;

BEGIN
    SELECT delivery_place_id FROM clients WHERE clients.id = NEW._from INTO del_place;
    SELECT id FROM providers WHERE providers.delivery_place_id = del_place INTO provider;
    SELECT storages.sausages_weight
    FROM storages
    WHERE (storages.sausage_id = NEW.sausage_id AND
           storages.factory_id = (SELECT providers.factory_id FROM providers WHERE (providers.id = provider)))
    INTO weight;
    IF (weight > NEW.sausages_weight) THEN
        UPDATE storages
        SET sausages_weight = weight - NEW.sausages_weight
        WHERE (storages.sausage_id = NEW.sausage_id AND
               storages.factory_id =
               (SELECT providers.factory_id FROM providers WHERE (providers.id = provider)));
        IF EXISTS(SELECT *
                  FROM order_schedule
                  WHERE order_schedule.provider_id = provider
                    AND order_schedule.sausage_id =
                        NEW.sausage_id
                    AND order_schedule.del_time =
                        (current_date + 1)) THEN
            UPDATE order_schedule
            SET sausages_weight = sausages_weight + NEW.sausages_weight
            WHERE order_schedule.provider_id = provider
              AND order_schedule.sausage_id =
                  NEW.sausage_id
              AND order_schedule.del_time =
                  current_date + 1;
        ELSE
            INSERT INTO order_schedule(provider_id, sausage_id, sausages_weight, del_time)
            VALUES (provider, NEW.sausage_id, NEW.sausages_weight, current_date + 1);
        END IF;
    ELSE
        RAISE EXCEPTION 'We do not have this product in our storage, it would be soon';
    END IF;
    NEW._to = provider;
    NEW.ord_time = localtimestamp;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER make_Schedule
    BEFORE INSERT
    ON orders
    FOR EACH ROW
EXECUTE PROCEDURE make_Schedule();
--------------------------------------------------------------------------------------------------------------


--------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION payment() RETURNS TRIGGER AS
$$
DECLARE
    client_payment   integer;
    price            integer;
    factory          integer;
    provider_payment integer;
BEGIN
    SELECT client_id
    FROM clients_payments
    WHERE (client_id = NEW._from AND provider_id = NEW._to AND dept_time::date = current_date AND
           paying = FALSE)
    INTO client_payment;
    SELECT sausages.price FROM sausages WHERE (sausages.id = NEW.sausage_id) INTO price;
    IF (client_payment IS NOT NULL AND client_payment <> 0) THEN
        UPDATE clients_payments
        SET sum = sum + price * NEW.sausages_weight
        WHERE client_id = client_payment;
    ELSE
        INSERT INTO clients_payments(client_id, provider_id, sum, dept_time, paying, payment_date)
        VALUES (NEW._from, NEW._to, price * NEW.sausages_weight, current_date, FALSE, NULL);
    END IF;

    SELECT factory_id FROM providers WHERE providers.id = NEW._to INTO factory;
    SELECT provider_id
    FROM providers_payments
    WHERE (provider_id = NEW._to AND factory_id = factory AND dept_time = current_date AND paying = FALSE)
    INTO provider_payment;
    IF (provider_payment IS NOT NULL AND provider_payment <> 0) THEN
        UPDATE providers_payments
        SET sum = sum + price * NEW.sausages_weight
        WHERE provider_id = provider_payment;
    ELSE
        INSERT INTO providers_payments(provider_id, factory_id, sum, dept_time, paying, payment_date)
        VALUES (NEW._to, factory, price * NEW.sausages_weight, current_date, FALSE, NULL);
    END IF;
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER pay
    AFTER INSERT
    ON orders
    FOR EACH ROW
EXECUTE PROCEDURE payment();
--------------------------------------------------------------------------------------------------------------


-- ---------------------------------------------------------------------------------------------------------------
-- CREATE OR REPLACE FUNCTION client_pay() RETURNS TRIGGER AS
-- $$
-- DECLARE
--     del_place integer;
--     provider  integer;
-- BEGIN
--     SELECT delivery_place_id FROM clients WHERE clients.id = NEW.client_id INTO del_place;
--     SELECT id FROM providers WHERE providers.delivery_place_id = del_place INTO provider;
--     NEW.provider_id = provider;
--     NEW.paying = FALSE;
--     NEW.payment_date = current_date;
--     RETURN NEW;
-- END
-- $$ LANGUAGE plpgsql;
--
-- CREATE TRIGGER client_pay
--     BEFORE UPDATE
--     ON clients_payments
--     FOR EACH ROW
-- EXECUTE PROCEDURE client_pay();
-- ---------------------------------------------------------------------------------------------------------------


-- ---------------------------------------------------------------------------------------------------------------
-- CREATE OR REPLACE FUNCTION provider_pay() RETURNS TRIGGER AS
-- $$
-- DECLARE
--     factory integer;
-- BEGIN
--     SELECT factory_id FROM providers WHERE providers.id = NEW.provider_id INTO factory;
--     NEW.factory_id = factory;
--     NEW.paying = TRUE;
--     NEW.payment_date = current_date;
--     RETURN NEW;
-- END
-- $$ language plpgsql;
--
-- CREATE TRIGGER provider_pay
--     BEFORE UPDATE
--     ON providers_payments
--     FOR EACH ROW
-- EXECUTE PROCEDURE provider_pay();
-- ---------------------------------------------------------------------------------------------------------------


--------------------------------------------------------------------------------------------------------------
-- client returning to provider
CREATE OR REPLACE FUNCTION return_client() RETURNS TRIGGER AS
$$
DECLARE
    del_place integer;
    provider  integer;
BEGIN
    SELECT delivery_place_id FROM clients WHERE clients.id = NEW._from INTO del_place;
    SELECT id FROM providers WHERE providers.delivery_place_id = del_place INTO provider;
    NEW._to = provider;
    NEW.ret_time = localtime;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER return_client
    BEFORE INSERT
    ON return_client
    FOR EACH ROW
EXECUTE PROCEDURE return_client();
--------------------------------------------------------------------------------------------------------------


--------------------------------------------------------------------------------------------------------------
-- provider returning to factory
CREATE OR REPLACE FUNCTION return_provider() RETURNS TRIGGER AS
$$
DECLARE
BEGIN
    SELECT factory_id FROM providers WHERE providers.id = NEW._from INTO NEW._to;
    NEW.ret_time = localtime;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER return_provider
    BEFORE INSERT
    ON return_provider
    FOR EACH ROW
EXECUTE PROCEDURE return_provider();
--------------------------------------------------------------------------------------------------------------