.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

=====================================
MicroServizi
=====================================

.. 292 Testing

- Requisiti funzionali, Prodotto, Architettura, Processo, Collaudo, Deployment.

.. 46 159 Domain model, 54 subdomain, 151 DDD, 152 aggregate, boundaries, 160 domain events


- Struttura, interazione, comportamento

.. 8  ..38 esagonale style

- Requisiti non funzionali, Architettura esagonale, Architettura Microservice, Scale cube

.. 29 

- Architettura, processo, organizzazione (Conway ribaltata)

.. 65 IPC, 67 interaction styles, 97 Transactional messaging, 100 EventuatedTram

.. 110 sages, 112 Distributed transactions, 127 Anomalies

- From ACID to ACD

.. 259

- API gateway

++++++++++++++++++++++++++++++++++++++
Lista Pattern libro
++++++++++++++++++++++++++++++++++++++
- Application architecture patterns
  
  - Monolithic architecture (40)
  - Microservice architecture (40)
- Decomposition patterns
  
  - Decompose by business capability (51)
  - Decompose by subdomain (54)
- Messaging style patterns
  
  - Messaging (85)
  - Remote procedure invocation (72)
- Reliable communications patterns
  
  - Circuit breaker (78)
- Service discovery patterns
  
  - 3rd party registration (85)
  - Client-side discovery (83)
  - Self-registration (82)
  - Server-side discovery (85)
- Transactional messaging patterns

   - Polling publisher (98)
   - Transaction log tailing (99)
   - Transactional outbox (98)
- Data consistency patterns
  
   - Saga (114)
- Business logic design patterns

  - Aggregate (150)
  - Domain event (160)
  - Domain model (150)
  - Event sourcing (184)
  - Transaction script (149)
- Querying patterns

  - API composition (223)
  - Command query responsibility segregation (228)
- External API patterns

  - API gateway (259)
  - Backends for frontends (265)
- Testing patterns
  
  - Consumer-driven contract test (302)
  - Consumer-side contract test (303)
  - Service component test (335)
- Security patterns
  
  - Access token (354)
- Cross-cutting concerns patterns
  
  - Externalized configuration (361)
  - Microservice chassis (379)
- Observability patterns
  
  - Application metrics (373)
  - Audit logging (377)
  - Distributed tracing (370)
  - Exception tracking (376)
  - Health check API (366)
  - Log aggregation (368)
- Deployment patterns
  
  - Deploy a service as a container (393)
  - Deploy a service as a VM (390)
  - Language-specific packaging format (387)
  - Service mesh (380)
  - Serverless deployment (416)
  - Sidecar (410)
- Refactoring to microservices patterns
  
  - Anti-corruption layer (447)
  - Strangler application (432)



++++++++++++++++++++++++++++++++++++++
Lista Pattern da fare
++++++++++++++++++++++++++++++++++++++

- Microservice architecture (40)
- Decompose by subdomain (54)
- Domain model (150)
- Aggregate (150)
- Messaging (85)
- Circuit breaker (78)
- Transactional messaging patterns