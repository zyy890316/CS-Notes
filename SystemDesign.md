# System design aggregated github articles
https://github.com/donnemartin/system-design-primer

# DDIA
https://vonng.gitbooks.io/ddia-cn/content/ch1.html

# 一篇文章解决所有system design面试
https://blog.csdn.net/AuburnTigers/article/details/102601151

# Steps
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

# Quorum
* Quorum is achieved when nodes follow the below protocol: R+W>N
** N = nodes in the quorum group
** W = minimum write nodes
** R = minimum read nodes
* Best performance (throughput/availability) when 1<r<w<n, because reads are more frequent than writes in most applications


# Data replication
* Probabilistic protocols for eventual consistency: Gossip, Epidemic broadcast
* Consensus protocols for strong consistency: 2/3 phase commit, chain replication
