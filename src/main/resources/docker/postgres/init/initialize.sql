-- CREATE TABLE category
-- (
--     id   SERIAL      NOT NULL,
--     name VARCHAR(20) NOT NULL,
--     PRIMARY KEY (id)
-- );
--
-- CREATE TABLE tip
-- (
--     id          SERIAL      NOT NULL,
--     content     VARCHAR(30) NOT NULL,
--     update_datetime TIMESTAMP   NOT NULL,
--     PRIMARY KEY (id)
-- );
--
-- CREATE TABLE restaurant
-- (
--     id              SERIAL      NOT NULL,
--     category        INT         NOT NULL,
--     name            VARCHAR(30) NOT NULL,
--     intro           VARCHAR(70) NOT NULL,
--     way_to_go       VARCHAR(50) NOT NULL,
--     zipcode         VARCHAR(7)  NOT NULL,
--     state           VARCHAR(15) NOT NULL,
--     city            VARCHAR(15) NOT NULL,
--     address         VARCHAR(50) NOT NULL,
--     latitude        REAL        NOT NULL,
--     longitude       REAL        NOT NULL,
--     create_datetime TIMESTAMP   NOT NULL,
--     update_datetime TIMESTAMP   NOT NULL,
--
--     PRIMARY KEY (id),
--     CONSTRAINT fk_category FOREIGN KEY (category) REFERENCES category (id)
-- );
--
-- CREATE TABLE open_time
-- (
--     ID              SERIAL NOT NULL,
--     restaurant_id   INT    NOT NULL,
--     operation_start time   NOT NULL,
--     operation_end   time   NOT NULL,
--     break_start     time,
--     break_end       time,
--
--     PRIMARY KEY (ID),
--     CONSTRAINT fk_restaurant_id FOREIGN KEY (restaurant_id) REFERENCES RESTAURANT (id)
-- );
--
-- CREATE TABLE menu
-- (
--     id            SERIAL       NOT NULL,
--     restaurant_id INT          NOT NULL,
--     name          VARCHAR(30)  NOT NULL,
--     image         VARCHAR(200) NOT NULL,
--     price         INT          NOT NULL,
--     update_datetime TIMESTAMP   NOT NULL,
--     PRIMARY KEY (id),
--     CONSTRAINT fk_restaurant_id FOREIGN KEY (restaurant_id) REFERENCES restaurant (id)
-- );
--
-- CREATE TABLE restaurant_tip
-- (
--     restaurant_id INT NOT NULL,
--     tip_id        INT NOT NULL,
--     PRIMARY KEY (restaurant_id, tip_id),
--     CONSTRAINT fk_restaurant_id FOREIGN KEY (restaurant_id) REFERENCES restaurant (id),
--     CONSTRAINT fk_tip_id FOREIGN KEY (tip_id) REFERENCES tip (id)
-- );
--
-- ALTER SEQUENCE category_id_seq RESTART WITH 1000;
-- ALTER SEQUENCE tip_id_seq RESTART WITH 1000;
-- ALTER SEQUENCE restaurant_id_seq RESTART WITH 1000;
-- ALTER SEQUENCE open_time_id_Seq RESTART WITH 1000;
-- ALTER SEQUENCE menu_id_seq RESTART WITH 1000;