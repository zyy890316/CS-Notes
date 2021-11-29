# System Design Learning:
* Distributed Systems in One Lesson: https://learning.oreilly.com/videos/distributed-systems-in/9781491924914/
* System design aggregated github articles: https://github.com/donnemartin/system-design-primer
* DDIA: https://vonng.gitbooks.io/ddia-cn/content/ch1.html
* 一篇文章解决所有system design面试: https://blog.csdn.net/AuburnTigers/article/details/102601151

# Restful API Design:
* Endpoint: https://api_domain/version/collection/
* Method:
** POST:	Create	201 (Created),404 (Not Found), 409 (Conflict) if resource already exists
** GET	Read	200 (OK)
** PUT	Update/Replace 200 (OK) or 204 (No Content). 404 (Not Found)
** PATCH Update/Modify
** DELETE 200 (OK). 404 (Not Found), if ID not found or invalid.

* Request Header:
** Authorization: access_token
** Accept: To request for response in a specified content-type
** Content-Type: Content type for the request body
* Request Parameters
** Filtering: type=news,photo&days=sunday
** Sorting: sort=create_time,-creator
** Paging: offset=100&limit=100
** Search/Query: q=AI
** Access Token: access_token=

# System Design Steps
1. Functional Requirements (APIs)
  * Define input parameters and return values
  * make several iterations about additional functionalities and future use
2. Non-functional Requirements
  * Scalability
  * Availability
  * Performance
  * Consistency
  * Cost and Maintainability
3. High-level design: from data flow perspective
  * How data gets in
  * how data gets out
  * how data stored in the system
4. Detailed Design: it is all about data (storage, transfer, processing)
5. Bottlenecks and tradeoffs

# Key Point
## Bloom Filter
https://www.youtube.com/watch?v=-jiOPKt7avE

## Rate limiting
* Token Bucket: Allow Burst
* Leaky Bucket: Smooth Burst

## Fault Tolarent
* Bulkhead: isolation between different down streams
* Circuit breaker

## Redis (key-value) as NoSQL Cache Solution
* In memory
* NoSQL but more than just key value map
* Implements Redis Sentinel as configuration service

### Use case
* Optimization for Cache
* Use as in memory database
* Redis supporting sorted sets which can be used for leaderboard
* Message Broker
* Data Stream Engine
https://www.youtube.com/watch?v=jgpVdJB2sKQ
https://www.youtube.com/watch?v=LNsqFf7Pu4I&list=PL9nWRykSBSFhv9Ptvl8Bu8CpxedGpHDc8&index=5

## Cassandra (wide-col)

## Kafka
https://www.youtube.com/watch?v=JalUUBKdcA0

## Short Polling vs Long Polling vs WebSockets
https://www.youtube.com/watch?v=ZBM28ZPlin8

## ZooKeeper:
https://learning.oreilly.com/videos/distributed-systems-in/9781491924914/9781491924914-video215282/
in memory metadata storage with leader follower pattern, follower accept read and write, write will be redirected to leader node.
* Use case:
** Leader Elections: follower register themselves in a ZNode and watch for result, usually in 10s range, not fast
** Distributed locks

## Elasticsearch
Elasticsearch is a search engine based on the Apache Lucene library. It provides a distributed, multitenant-capable full-text search engine with an HTTP web interface and schema-free JSON documents.
Kibana is a proprietary data visualization dashboard software for Elasticsearch.

## Quorum
* Quorum is achieved when nodes follow the below protocol: R+W>N
** N = nodes in the quorum group
** W = minimum write nodes
** R = minimum read nodes
* Best performance (throughput/availability) when 1<r<w<n, because reads are more frequent than writes in most applications

## Data replication
* Probabilistic protocols for eventual consistency: Gossip, Epidemic broadcast
* Consensus protocols for strong consistency: 2/3 phase commit, chain replication

## Consensus Protocol: Paxos
* Core idea: Each propose have a sequence number, acceptor only accept higher sequence than what they see so far
* Other protocol: Raft, blockchain(like Paxos, but have node lying to caller)
* Use case:
** lightweight transactions in Cassandra``` INSERT ... IF NOT EXISTS```
** Master Election
