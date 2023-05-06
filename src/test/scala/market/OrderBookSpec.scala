package market

import dev.sonicbet.market.{BackOrder, LayOrder, LotSaleOrder, OrderBook}
import org.scalatest.funsuite.AnyFunSuite


class OrderBookSpec extends AnyFunSuite {
  test("Ordering and removing works correctly") {
    val lay0 = LayOrder("lay0", uid = 1, amount = BigDecimal(100000), odds = BigDecimal(0.5), outcome = 1, stamp = 200)
    val lay1 = LayOrder("lay1", uid = 1, amount = BigDecimal(200000), odds = BigDecimal(0.4), outcome = 1, stamp = 100)
    val lay2 = LayOrder("lay2", uid = 1, amount = BigDecimal(300000), odds = BigDecimal(0.3), outcome = 1, stamp = 1000)
    val lay3 = LayOrder("lay3", uid = 1, amount = BigDecimal(300000), odds = BigDecimal(0.3), outcome = 1, stamp = 2000)

    val back0 = BackOrder("back0", uid = 1, amount = BigDecimal(100000), odds = BigDecimal(0.5), outcome = 1, stamp = 200)
    val back1 = LotSaleOrder("back1", uid = 1, amount = BigDecimal(200000), odds = BigDecimal(0.4), outcome = 1, stamp = 100)
    val back2 = LotSaleOrder("back2", uid = 1, amount = BigDecimal(300000), odds = BigDecimal(0.3), outcome = 1, stamp = 1000)
    val back3 = BackOrder("back3", uid = 1, amount = BigDecimal(300000), odds = BigDecimal(0.3), outcome = 1, stamp = 2000)

    var book = OrderBook.create
    book = book.withLayOrder(lay3)
    book = book.withLayOrder(lay2)
    book = book.withLayOrder(lay1)
    book = book.withLayOrder(lay0)

    book = book.withBackOrder(back3)
    book = book.withBackOrder(back2)
    book = book.withBackOrder(back1)
    book = book.withBackOrder(back0)

    assert(book.lays.toList.map(_.id) == List("lay0", "lay1", "lay2", "lay3"))
    assert(book.backs.toList.map(_.id) == List("back2", "back3", "back1", "back0"))

    book = book.withoutOrder("lay1")
    book = book.withoutOrder("back3")

    assert(book.lays.toList.map(_.id) == List("lay0", "lay2", "lay3"))
    assert(book.backs.toList.map(_.id) == List("back2", "back1", "back0"))
  }
}
