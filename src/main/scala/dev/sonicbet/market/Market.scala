package dev.sonicbet.market


/*
Can be filled by:

Back order:
2 oscillating market maker
3 opposite lay order (lay gets evenly partitioned tokens on all other outcomes) <- when MM is exhausted/inable to
4 1 to N other back orders until total gets to 100% <- when MM is exhausted/inable to

Lay order:
3 opposite back order (lay gets tokens on all other outcomes)
5 N other lay/tokensell orders including MM lay orders (100% - their sum determines our stake)

Tokensell order:
2 oscillating market maker
3 back order on same outcome <- when MM is exhausted/inable to
5 other lay order (this one being a part of N back/tokensell orders)
*/

/*
1. Add new order to book
2. Do MM oscillation
3. Fulfill opposite orders on same outcomes
4. Cross-fulfill back orders on different outcomes
5. Cross-fulfill lay orders on different outcomes
6. Increase MM liquidity
7. Repeat from 1 until no more trades
*/

/*
On cross-fulfilling of lay orders:
- we select the largest lay order which can cover the most of other orders
- we use that largest lay order to buy the same max amount of tokens from all other lay orders
- all other limit lay orders get their tokens as if they were bought by corresponding back order on same outcome
*/

case class Market(books: List[OrderBook], lmsr: LMSR)
