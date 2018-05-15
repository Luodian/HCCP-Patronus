CREATE TABLE CLIENTNODES(
		user_name  VARCHAR(10),
        user_id CHAR(10) PRIMARY KEY,
        email VARCHAR(30),
        password VARCHAR(20)
);

CREATE TABLE GROUPS(
	 group_name varchar(10),
     data_type varchar(10),
     group_id char(10) PRIMARY KEY,
     member_nums int,
     creator_id char(10),
     create_date date,
     foreign key (creator_id) references clientnodes(user_id)
);

CREATE TABLE COMPUTETASK(
	task_id char(10) primary key,
    data_type varchar(10),
    start_time date,
    end_time date,
    cost real,
    state varchar(10),
    initiator_id char(10),
    foreign key (initiator_id) references clientnodes(user_id)
);

CREATE TABLE DATANODES(
	user_id char(10),
    data_name varchar(10),
    data_type varchar(10),
    row_nums int,
    attr_nums int,
    primary key (user_id, data_name),
    foreign key(user_id) references clientnodes(user_id)
    
);