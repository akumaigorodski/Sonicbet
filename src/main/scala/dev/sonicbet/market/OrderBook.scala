package dev.sonicbet.market

import dev.sonicbet.market.OrderBook.BackOrLotSaleOrder
import scala.collection.immutable.TreeSet
import scala.math.Ordering


object OrderBook:
  type OddsThenTime = (BigDecimal, Long)
  type BackOrLotSaleOrder = BackOrder | LotSaleOrder
  val DESCENDING: Ordering[LayOrder] = Ordering[OddsThenTime].on(order => -order.odds -> order.stamp)
  val ASCENDING: Ordering[BackOrLotSaleOrder] = Ordering[OddsThenTime].on(order => order.odds -> order.stamp)
  def create: OrderBook = OrderBook(TreeSet.empty(ASCENDING), TreeSet.empty(DESCENDING), byId = Map.empty)

case class OrderBook(backs: TreeSet[BackOrLotSaleOrder], lays: TreeSet[LayOrder], byId: Map[String, Order] = Map.empty):
  def withBackOrder(order: BackOrLotSaleOrder): OrderBook = copy(byId = byId.updated(order.id, order), backs = backs + order)
  def withLayOrder(order: LayOrder): OrderBook = copy(byId = byId.updated(order.id, order), lays = lays + order)

  def withoutOrder(orderId: String): OrderBook = byId.get(orderId) match
    case Some(order: BackOrLotSaleOrder) => copy(backs = backs - order, byId = byId - orderId)
    case Some(order: LayOrder) => copy(lays = lays - order, byId = byId - orderId)
    case None => this

sealed trait Order:
  val amount: BigDecimal
  val odds: BigDecimal
  val outcome: Int
  val stamp: Long
  val id: String
  val uid: Long

case class LotSaleOrder(id: String, uid: Long, amount: BigDecimal, odds: BigDecimal, outcome: Int, stamp: Long) extends Order
case class BackOrder(id: String, uid: Long, amount: BigDecimal, odds: BigDecimal, outcome: Int, stamp: Long) extends Order
case class LayOrder(id: String, uid: Long, amount: BigDecimal, odds: BigDecimal, outcome: Int, stamp: Long) extends Order
