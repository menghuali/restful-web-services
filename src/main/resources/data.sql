-- User details
delete from post;
delete from user_details;
insert into
    user_details(id, name, birth_date)
values
    (10001, 'Bruce Wayne', '1915-04-07');

insert into
    user_details(id, name, birth_date)
values
    (10002, 'Peter Parker', '2001-08-10');

insert into
    user_details(id, name, birth_date)
values
    (10003, 'Clark Kent', '1938-02-01');

-- Post
insert into
    post(id, description, user_id)
values
(10001, 'I am vengeance. I am the night.', 10001);

insert into
    post(id, description, user_id)
values
(10002, 'It is not who I am underneath, but what I do that defines me.', 10001);

insert into
    post(id, description, user_id)
values
(10003, 'With great power comes great responsibility.', 10002);

insert into
    post(id, description, user_id)
values
(10004, 'No one dies.', 10002);

insert into
    post(id, description, user_id)
values
(10005, 'Truth, justice, and the American way.', 10003);

insert into
    post(id, description, user_id)
values
(10006, 'You will give the people of earth an ideal to strive towards.', 10003);