CREATE TABLE IF NOT EXISTS refresh_token (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    refresh_token_key VARCHAR(255) NOT NULL,
    revoked TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_refresh_token_id PRIMARY KEY (id),
    CONSTRAINT uq_refresh_token_key UNIQUE KEY (refresh_token_key)
);

CREATE TABLE IF NOT EXISTS user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    account_non_expired TINYINT NOT NULL DEFAULT 1,
    account_non_locked TINYINT NOT NULL DEFAULT 1,
    credentials_non_expired TINYINT NOT NULL DEFAULT 1,
    enabled TINYINT NOT NULL DEFAULT 1,    
    created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_user_id PRIMARY KEY (id),
    CONSTRAINT uq_user_username UNIQUE KEY(username)
);

CREATE TABLE IF NOT EXISTS user_info (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    first_name	VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    mobile_number VARCHAR(255) NOT NULL,
    email_address VARCHAR(255) NOT NULL,
    created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_user_info_id PRIMARY KEY (id),
    CONSTRAINT fk_user_info_user_id FOREIGN kEY(user_id) REFERENCES user(id),
    CONSTRAINT uq_user_info_mobile_number UNIQUE KEY(mobile_number),
    CONSTRAINT uq_user_info_email_address UNIQUE KEY(email_address)
);

CREATE TABLE IF NOT EXISTS status (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_status_id PRIMARY KEY (id),
    CONSTRAINT uq_status_name UNIQUE KEY(name)
);

CREATE TABLE IF NOT EXISTS todo (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    description	TEXT NOT NULL,
    status_id INT NOT NULL,
    created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_todo_id PRIMARY KEY(id),
    CONSTRAINT fk_todo_user_id FOREIGN kEY(user_id) REFERENCES user(id),
    CONSTRAINT fk_todo_status_id FOREIGN kEY(status_id) REFERENCES status(id)
);