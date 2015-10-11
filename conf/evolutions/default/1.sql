# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table blog_post (
  id                        bigint not null,
  subject                   varchar(255) not null,
  content                   TEXT not null,
  is_active                 BOOLEAN,
  author_id                 bigint,
  create_time               DATE not null,
  close_time                DATE,
  closer_id                 bigint,
  comment_count             bigint,
  constraint uq_blog_post_subject unique (subject),
  constraint pk_blog_post primary key (id))
;

create table post_comment (
  id                        bigint not null,
  blog_post_id              bigint,
  author_id                 bigint,
  constraint pk_post_comment primary key (id))
;

create table user (
  id                        bigint not null,
  codename                  varchar(255) not null,
  org                       varchar(255) not null,
  sha_password              varbinary(255) not null,
  constraint uq_user_codename unique (codename),
  constraint pk_user primary key (id))
;

create sequence blog_post_seq;

create sequence post_comment_seq;

create sequence user_seq;

alter table blog_post add constraint fk_blog_post_author_1 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_blog_post_author_1 on blog_post (author_id);
alter table blog_post add constraint fk_blog_post_closer_2 foreign key (closer_id) references user (id) on delete restrict on update restrict;
create index ix_blog_post_closer_2 on blog_post (closer_id);
alter table post_comment add constraint fk_post_comment_blogPost_3 foreign key (blog_post_id) references blog_post (id) on delete restrict on update restrict;
create index ix_post_comment_blogPost_3 on post_comment (blog_post_id);
alter table post_comment add constraint fk_post_comment_author_4 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_post_comment_author_4 on post_comment (author_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists blog_post;

drop table if exists post_comment;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists blog_post_seq;

drop sequence if exists post_comment_seq;

drop sequence if exists user_seq;

