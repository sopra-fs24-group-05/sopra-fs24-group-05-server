-- Drop existing tables if they exist
DROP TABLE IF EXISTS ITEM;
DROP TABLE IF EXISTS TOPIC_CHATPOOLS;
DROP TABLE IF EXISTS TOPIC;

-- Create the TOPIC table
CREATE TABLE TOPIC (
                       topic_id INTEGER AUTO_INCREMENT PRIMARY KEY,
                       topic_name VARCHAR(255) NOT NULL UNIQUE,
                       creation_date TIMESTAMP,
                       owner_id INTEGER,
                       father_topic_id BIGINT,
                       son_topic_id BIGINT,
                       edit_allowed BOOLEAN NOT NULL,
                       description VARCHAR(255),
                       search_count INTEGER DEFAULT 0
);

-- Create the ITEM table
CREATE TABLE ITEM (
                      id INTEGER AUTO_INCREMENT PRIMARY KEY,
                      item_name VARCHAR(255) NOT NULL,
                      content VARCHAR(255) NOT NULL,
                      creation_date TIMESTAMP,
                      score DOUBLE NOT NULL DEFAULT 0.0,
                      likes INTEGER NOT NULL DEFAULT 0,
                      topic_id INTEGER,
                      popularity INTEGER DEFAULT 0,
                      CONSTRAINT fk_topic
                          FOREIGN KEY (topic_id)
                              REFERENCES TOPIC(topic_id)
);

-- Create the TOPIC_CHATPOOLS table
CREATE TABLE TOPIC_CHATPOOLS (
                                 topic_id INTEGER,
                                 message VARCHAR(255),
                                 CONSTRAINT fk_chatpool_topic
                                     FOREIGN KEY (topic_id)
                                         REFERENCES TOPIC(topic_id)
);

CREATE TABLE IF NOT EXISTS user (
                                    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    username VARCHAR(255) NOT NULL UNIQUE,
                                    password VARCHAR(255) NOT NULL,
                                    status VARCHAR(255) NOT NULL,
                                    token VARCHAR(255) NOT NULL UNIQUE,
                                    identity VARCHAR(255) NOT NULL,
                                    avatar TEXT NOT NULL,
                                    create_date TIMESTAMP,
                                    birthday TIMESTAMP
);

CREATE TABLE IF NOT EXISTS follow_item_list (
                                                user_id BIGINT NOT NULL,
                                                follow_item_id BIGINT NOT NULL,
                                                FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE IF NOT EXISTS follow_user_list (
                                                user_id BIGINT NOT NULL,
                                                follow_user_id BIGINT NOT NULL,
                                                FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE IF NOT EXISTS follow_topic_list (
                                                 user_id BIGINT NOT NULL,
                                                 follow_topic_id BIGINT NOT NULL,
                                                 FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE IF NOT EXISTS comments (
                                        comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        comment_owner_id BIGINT NOT NULL,
                                        comment_owner_name VARCHAR(255) NOT NULL,
                                        comment_item_id BIGINT NOT NULL,
                                        score BIGINT,
                                        content TEXT,
                                        thumbs_up_num BIGINT NOT NULL,
                                        father_comment_id BIGINT
);

CREATE TABLE IF NOT EXISTS comment_liked_users (
                                                   comment_id BIGINT NOT NULL,
                                                   user_id BIGINT NOT NULL,
                                                   PRIMARY KEY (comment_id, user_id),
                                                   FOREIGN KEY (comment_id) REFERENCES comments(comment_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS chat_message (
                                            message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            item_id BIGINT NOT NULL,
                                            user_id BIGINT NOT NULL,
                                            user_name VARCHAR(255) NOT NULL,
                                            user_avatar VARCHAR(255) NOT NULL,
                                            content TEXT NOT NULL,
                                            message_time VARCHAR(255) NOT NULL
);