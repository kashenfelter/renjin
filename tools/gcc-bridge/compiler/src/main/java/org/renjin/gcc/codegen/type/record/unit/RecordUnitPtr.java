/**
 * Renjin : JVM-based interpreter for the R language for the statistical analysis
 * Copyright © 2010-2016 BeDataDriven Groep B.V. and contributors
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, a copy is available at
 * https://www.gnu.org/licenses/gpl-2.0.txt
 */
package org.renjin.gcc.codegen.type.record.unit;

import org.renjin.gcc.codegen.MethodGenerator;
import org.renjin.gcc.codegen.array.FatArrayExpr;
import org.renjin.gcc.codegen.condition.ConditionGenerator;
import org.renjin.gcc.codegen.expr.*;
import org.renjin.gcc.codegen.fatptr.FatPtr;
import org.renjin.gcc.codegen.fatptr.ValueFunction;
import org.renjin.gcc.codegen.type.UnsupportedCastException;
import org.renjin.gcc.codegen.type.fun.FunPtr;
import org.renjin.gcc.codegen.type.primitive.PrimitiveValue;
import org.renjin.gcc.codegen.type.record.RecordArrayExpr;
import org.renjin.gcc.codegen.type.record.RecordLayout;
import org.renjin.gcc.codegen.type.record.RecordValue;
import org.renjin.gcc.codegen.type.voidt.VoidPtrExpr;
import org.renjin.gcc.codegen.vptr.VPtrExpr;
import org.renjin.gcc.gimple.GimpleOp;
import org.renjin.gcc.gimple.type.GimpleIntegerType;
import org.renjin.gcc.gimple.type.GimplePrimitiveType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.repackaged.asm.Label;
import org.renjin.repackaged.asm.Type;

public class RecordUnitPtr implements RefPtrExpr {

  private RecordLayout layout;
  private JExpr ref;
  private FatPtr address;

  public RecordUnitPtr(RecordLayout layout, JExpr ref) {
    this.layout = layout;
    this.ref = ref;
  }

  public RecordUnitPtr(RecordLayout layout, JExpr ref, FatPtr address) {
    this.layout = layout;
    this.ref = ref;
    this.address = address;
  }

  public Type getJvmType() {
    return ref.getType();
  }
  
  @Override
  public void store(MethodGenerator mv, GExpr rhs) {
    ((JLValue) ref).store(mv, ((RecordUnitPtr) rhs).ref);
  }

  @Override
  public GExpr addressOf() {
    if(address == null) {
      throw new NotAddressableException();
    }
    return address;
  }

  public JExpr unwrap() {
    return ref;
  }

  @Override
  public void jumpIfNull(MethodGenerator mv, Label label) {
    ref.load(mv);
    mv.ifnull(label);
  }

  @Override
  public JExpr memoryCompare(MethodGenerator mv, PtrExpr otherPointer, JExpr n) {
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public PtrExpr realloc(MethodGenerator mv, JExpr newSizeInBytes) {
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public GExpr valueOf(GimpleType expectedType) {
    return new RecordValue(layout, ref);
  }

  @Override
  public ConditionGenerator comparePointer(MethodGenerator mv, GimpleOp op, GExpr otherPointer) {
    return new RefConditionGenerator(op, unwrap(), otherPointer.toRecordUnitPtrExpr(layout).unwrap());
  }

  @Override
  public FunPtr toFunPtr() {
    return FunPtr.NULL_PTR;
  }

  @Override
  public FatArrayExpr toArrayExpr() throws UnsupportedCastException {
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public PrimitiveValue toPrimitiveExpr(GimplePrimitiveType targetType) throws UnsupportedCastException {
    return new PrimitiveValue(GimpleIntegerType.unsigned(32), Expressions.identityHash(ref)).toPrimitiveExpr(targetType);
  }

  @Override
  public VoidPtrExpr toVoidPtrExpr() throws UnsupportedCastException {
    return new VoidPtrExpr(ref);
  }

  @Override
  public RecordArrayExpr toRecordArrayExpr() throws UnsupportedCastException {
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public VPtrExpr toVPtrExpr() throws UnsupportedCastException {
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public RecordUnitPtr toRecordUnitPtrExpr(RecordLayout layout) {
    return new RecordUnitPtr(this.layout, Expressions.cast(unwrap(), layout.getType()));
  }

  @Override
  public FatPtr toFatPtrExpr(ValueFunction valueFunction) {
    throw new UnsupportedCastException();
  }
}
