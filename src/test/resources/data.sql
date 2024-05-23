-- Insert sample data into TOPIC table
INSERT INTO TOPIC (topic_name, creation_date, owner_id, father_topic_id, son_topic_id, edit_allowed, description, search_count)
VALUES ('Sample Topic', NOW(), 1, NULL, NULL, TRUE, 'This is a sample topic', 0);

-- Insert sample data into ITEM table
INSERT INTO ITEM (item_name, content, creation_date, score, likes, topic_id, popularity)
VALUES ('Sample Item', 'This is a sample item', NOW(), 0.0, 0, 1, 0);
