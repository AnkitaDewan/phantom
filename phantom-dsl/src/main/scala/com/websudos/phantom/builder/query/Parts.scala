/*
 * Copyright 2013-2015 Websudos, Limited.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * - Explicit consent must be obtained from the copyright owner, Outworkers Limited before any redistribution is made.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.websudos.phantom.builder.query

import com.websudos.diesel.engine.query.multiparts.{QueryPart, MergedQueryList}
import com.websudos.phantom.builder.QueryBuilder


sealed class CQLMergeList(override val list: List[CQLQuery]) extends MergedQueryList[CQLQuery](list) {

  override def apply(list: List[CQLQuery]): MergedQueryList[CQLQuery] = new CQLMergeList(list)

  override def apply(str: String): CQLQuery = CQLQuery(str)
}

sealed abstract class CQLQueryPart[Part <: CQLQueryPart[Part]](override val list: List[CQLQuery]) extends QueryPart[Part, CQLQuery](list) {
  override def mergeList(list: List[CQLQuery]): MergedQueryList[CQLQuery] = new CQLMergeList(list)
}

sealed class UsingPart(override val list: List[CQLQuery] = Nil)
  (implicit builder: QueryBuilder) extends CQLQueryPart[UsingPart](list) {

  override def qb: CQLQuery = list match {
    case head :: tail => builder.Update.usingPart(list)
    case Nil => CQLQuery.empty
  }

  override def instance(l: List[CQLQuery]): UsingPart = new UsingPart(l)
}

object UsingPart {
  def empty()(implicit builder: QueryBuilder): UsingPart = new UsingPart()
}

sealed class WherePart(override val list: List[CQLQuery] = Nil)(
  implicit builder: QueryBuilder
) extends CQLQueryPart[WherePart](list) {
  override def qb: CQLQuery = builder.Update.clauses(list)

  override def instance(list: List[CQLQuery]): WherePart = new WherePart(list)
}

object WherePart {
  def empty()(implicit builder: QueryBuilder): WherePart = new WherePart(Nil)
}

sealed class LimitedPart(override val list: List[CQLQuery] = Nil)(
  implicit builder: QueryBuilder
) extends CQLQueryPart[LimitedPart](list) {
  override def qb: CQLQuery = builder.Update.clauses(list)

  override def instance(l: List[CQLQuery]): LimitedPart = new LimitedPart(l)
}

object LimitedPart {
  def empty()(implicit builder: QueryBuilder): LimitedPart = new LimitedPart(Nil)
}

sealed class OrderPart(override val list: List[CQLQuery] = Nil)(
  implicit builder: QueryBuilder
) extends CQLQueryPart[OrderPart](list) {
  override def qb: CQLQuery = list match {
    case head :: tail => builder.Select.Ordering.orderBy(list: _*)
    case Nil => CQLQuery.empty
  }

  override def instance(l: List[CQLQuery]): OrderPart = new OrderPart(l)
}

object OrderPart {
  def empty()(implicit builder: QueryBuilder): OrderPart = new OrderPart(Nil)
}

sealed class FilteringPart(override val list: List[CQLQuery] = Nil)(
  implicit builder: QueryBuilder
) extends CQLQueryPart[FilteringPart](list) {
  override def qb: CQLQuery = builder.Update.clauses(list)

  override def instance(l: List[CQLQuery]): FilteringPart = new FilteringPart(l)
}

object FilteringPart {
  def empty()(implicit builder: QueryBuilder): FilteringPart = new FilteringPart()
}

sealed class SetPart(override val list: List[CQLQuery] = Nil)(
  implicit builder: QueryBuilder
) extends CQLQueryPart[SetPart](list) {
  override def qb: CQLQuery = builder.Update.chain(list)

  override def instance(l: List[CQLQuery]): SetPart = new SetPart(l)
}

object SetPart {
  def empty()(implicit builder: QueryBuilder): SetPart = new SetPart()
}

sealed class CompareAndSetPart(override val list: List[CQLQuery] = Nil)(
  implicit builder: QueryBuilder
) extends CQLQueryPart[CompareAndSetPart](list) {
  override def qb: CQLQuery = builder.Update.clauses(list)

  override def instance(l: List[CQLQuery]): CompareAndSetPart = new CompareAndSetPart(l)
}

object CompareAndSetPart {
  def empty()(implicit builder: QueryBuilder): CompareAndSetPart = new CompareAndSetPart()
}

sealed class ColumnsPart(override val list: List[CQLQuery] = Nil)(
  implicit builder: QueryBuilder
) extends CQLQueryPart[ColumnsPart](list) {
  override def qb: CQLQuery = builder.Insert.columns(list)

  override def instance(l: List[CQLQuery]): ColumnsPart = new ColumnsPart(l)
}

object ColumnsPart {
  def empty()(implicit builder: QueryBuilder): ColumnsPart = new ColumnsPart()
}

sealed class ValuePart(override val list: List[CQLQuery] = Nil)(
  implicit builder: QueryBuilder
) extends CQLQueryPart[ValuePart](list) {
  override def qb: CQLQuery = builder.Insert.values(list)

  override def instance(l: List[CQLQuery]): ValuePart = new ValuePart(l)
}

object ValuePart {
  def empty()(implicit builder: QueryBuilder): ValuePart = new ValuePart()
}

sealed class LightweightPart(override val list: List[CQLQuery] = Nil)(
  implicit builder: QueryBuilder
) extends CQLQueryPart[LightweightPart](list) {
  override def qb: CQLQuery = builder.Update.clauses(list)

  override def instance(l: List[CQLQuery]): LightweightPart = new LightweightPart(l)
}

object LightweightPart {
  def empty()(implicit builder: QueryBuilder): LightweightPart = new LightweightPart()
}

sealed class WithPart(override val list: List[CQLQuery] = Nil)(
  implicit builder: QueryBuilder
) extends CQLQueryPart[WithPart](list) {
  override def qb: CQLQuery = builder.Update.clauses(list)

  override def instance(l: List[CQLQuery]): WithPart = new WithPart(l)
}

object WithPart {
  def empty()(implicit builder: QueryBuilder): WithPart = new WithPart()
}
