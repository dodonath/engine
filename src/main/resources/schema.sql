create table WORKFLOW_MASTER
(
   workflowId bigint AUTO_INCREMENT not null,
   workflowCode varchar(255) not null,
   workflowDescription varchar(255),
   totalSteps bigint not null,
   createdBy varchar(255),
   updatedBy varchar(255),
   createdAt bigint ,
   updatedAt bigint ,
   active boolean not null,
   primary key(workflowId)
);

create table WORKFLOW_INSTANCES_STATUS
(
   instanceId bigint AUTO_INCREMENT not null,
   workflowId bigint not null,
   instanceCode varchar(255) not null,
   currentStep varchar(255),
   percentCompletion double not null,
   overAllStatus varchar(255),
   startedAt bigint ,
   endedAt bigint ,
   active boolean not null,
   primary key(instanceId)
);

create table INSTANCES_DRILL_DOWN
(
   drilldownId bigint AUTO_INCREMENT not null,
   instanceId bigint not null,
   stepName varchar(255) not null,
   runStatus varchar(255) not null,
   errorStack text,
   startedAt bigint,
   endedAt bigint,
   active boolean not null,
   primary key(drilldownId)
);