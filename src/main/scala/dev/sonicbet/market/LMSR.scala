package dev.sonicbet.market

import java.math.BigDecimal as JBigDecimal
import ch.obermuhlner.math.big.BigDecimalMath
import dev.sonicbet.Params.{ZERO, mathContext}
import dev.sonicbet.*


object LMSR:
  def apply(subsidySat: Long, marketDims: Int): LMSR =
    val lots = List.fill(marketDims)(ZERO)
    LMSR(subsidySat, lots)

case class LMSR(subsidySat: Long, lotsPerMarket: List[BigDecimal] = Nil):
  val b: BigDecimal = subsidySat / math.log(lotsPerMarket.size)

  private def expBigDec(value: BigDecimal): BigDecimal =
    val parameter = value.bigDecimal.divide(b.bigDecimal, mathContext)
    BigDecimalMath.exp(parameter, mathContext)

  def prob(position: Int): BigDecimal =
    val target = lotsPerMarket(position)
    val totalSum = lotsPerMarket.map(expBigDec).sum
    expBigDec(target) / totalSum

  def lotsFromProb(position: Int, targetProb: BigDecimal): Double =
    val diff1 = targetProb * prob(position).oneMinus
    val diff2 = prob(position) * targetProb.oneMinus
    val doubleDiff = (diff1 / diff2).doubleValue
    b.doubleValue * math.log(doubleDiff)

  def probFromAmount(position: Int, amount: BigDecimal): BigDecimal =
    (prob(position).oneMinus / expBigDec(amount).doubleValue).oneMinus

  def update(lots: List[BigDecimal] = Nil): (LMSR, BigDecimal) =
    val lots1: List[BigDecimal] = lots.zip(lotsPerMarket).map(_ + _)
    val prevPrice = b * BigDecimalMath.log(lotsPerMarket.map(expBigDec).sum.bigDecimal, mathContext)
    val nextPrice = b * BigDecimalMath.log(lots1.map(expBigDec).sum.bigDecimal, mathContext)
    (LMSR(subsidySat, lots1), nextPrice - prevPrice)
