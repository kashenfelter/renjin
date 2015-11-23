package org.renjin.gcc.codegen.expr;

import org.objectweb.asm.MethodVisitor;
import org.renjin.gcc.gimple.type.GimpleType;

/**
 * Generates the complex conjugate of a complex number 
 * 
 * <p>The conjugate is the number with equal real part and imaginary part equal in magnitude but opposite in sign. 
 * For example, the complex conjugate of 3 + 4i is 3 − 4i.
 */
public class ConjugateGenerator extends AbstractExprGenerator {
  
  private ExprGenerator operand;

  public ConjugateGenerator(ExprGenerator operand) {
    this.operand = operand;
  }

  @Override
  public GimpleType getGimpleType() {
    return operand.getGimpleType();
  }

  @Override
  public ExprGenerator realPart() {
    return operand.realPart();
  }

  @Override
  public ExprGenerator imaginaryPart() {
    return new NegateGenerator(operand.imaginaryPart());
  }

  @Override
  public void emitPushComplexAsArray(MethodVisitor mv) {
    super.emitPushComplexAsArray(mv);
  }
}
