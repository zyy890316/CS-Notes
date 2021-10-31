# System design aggregated github articles
https://github.com/donnemartin/system-design-primer

# DDIA
https://vonng.gitbooks.io/ddia-cn/content/ch1.html

# 一篇文章解决所有system design面试
https://blog.csdn.net/AuburnTigers/article/details/102601151

# Key Point
## Bloom Filter
https://www.youtube.com/watch?v=-jiOPKt7avE
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
