package market

import dev.sonicbet.*
import com.softwaremill.quicklens.*
import dev.sonicbet.Params.ZERO
import dev.sonicbet.market.LMSR
import org.scalatest.funsuite.AnyFunSuite

class LMSRSpec extends AnyFunSuite {
  test("Basic probabilities") {
    val market = LMSR(subsidySat = 10000, marketDims = 4)
    assert(market.prob(position = 0) == market.prob(position = 3))
    assert(market.prob(position = 0) == 0.25)
  }

  test("Calculate lots and amounts for probabilities") {
    val market = LMSR(subsidySat = 10000, marketDims = 4)
    val lotsToGetTo = market.lotsFromProb(position = 2, 0.5)
    val (market1, amount) = market.update(ZERO :: ZERO :: BigDecimal(lotsToGetTo) :: ZERO :: Nil)
    assert(market1.prob(position = 2) ~= 0.5)
    assert(market.probFromAmount(position = 2, amount) ~= 0.5)
    assert(market1.probFromAmount(position = 2, -amount) ~= 0.25)
  }

  test("Multiple outcome purchase costs the same amount") {
    val market = LMSR(subsidySat = 10000, marketDims = 4)
    val lotsToGetTo = 500
    val (market123, amount123) = market.update(ZERO :: BigDecimal(lotsToGetTo) :: BigDecimal(lotsToGetTo) :: BigDecimal(lotsToGetTo) :: Nil)
    val (market1, amount1) = market.update(ZERO :: ZERO :: ZERO :: BigDecimal(lotsToGetTo) :: Nil)
    val (market2, amount2) = market1.update(ZERO :: ZERO :: BigDecimal(lotsToGetTo) :: ZERO :: Nil)
    val (market3, amount3) = market2.update(ZERO :: BigDecimal(lotsToGetTo) :: ZERO :: ZERO :: Nil)
    assert(amount1 + amount2 + amount3 == amount123)
    assert(market123.prob(0) == market3.prob(0))
    assert(market123.prob(1) == market3.prob(1))
    assert(market123.prob(2) == market3.prob(2))
    assert(market123.prob(3) == market3.prob(3))
  }

  test("Increasing subsidy reduces probability range") {
    val market = LMSR(subsidySat = 10000, marketDims = 4)
    assert(market.probFromAmount(position = 1, 25000) ~= 0.97656)
    val market1 = market.modify(_.subsidySat).using(_ + 90000)
    assert(market1.probFromAmount(position = 1, 25000) ~= 0.46966)
  }
}
