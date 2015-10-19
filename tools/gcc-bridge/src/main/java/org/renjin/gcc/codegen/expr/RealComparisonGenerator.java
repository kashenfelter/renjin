package org.renjin.gcc.codegen.expr;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.renjin.gcc.gimple.GimpleOp;

public class RealComparisonGenerator implements ValueGenerator, ConditionGenerator {

  private GimpleOp op;
  private ValueGenerator x;
  private ValueGenerator y;

  public RealComparisonGenerator(GimpleOp op, ValueGenerator x, ValueGenerator y) {
    this.op = op;
    this.x = x;
    this.y = y;
  }

  @Override
  public Type primitiveType() {
    return Type.INT_TYPE;
  }

  @Override
  public void emitPush(MethodVisitor mv) {
    
  }


  @Override
  public void emitJump(MethodVisitor mv, Label trueLabel) {

  }


  public boolean isDouble() {
    return primitiveType().equals(Type.DOUBLE_TYPE);
  }
}
