package org.renjin.gcc.codegen.ret;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.renjin.gcc.codegen.call.CallGenerator;
import org.renjin.gcc.codegen.expr.ExprGenerator;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.GimpleVoidType;

import java.util.List;

public class VoidReturnGenerator implements ReturnGenerator {

  @Override
  public Type getType() {
    return Type.VOID_TYPE;
  }

  @Override
  public GimpleType getGimpleType() {
    return new GimpleVoidType();
  }

  @Override
  public void emitReturn(MethodVisitor mv, ExprGenerator valueGenerator) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void emitVoidReturn(MethodVisitor mv) {
    mv.visitInsn(Opcodes.RETURN);
  }

  @Override
  public ExprGenerator callExpression(CallGenerator callGenerator, List<ExprGenerator> arguments) {
    throw new UnsupportedOperationException("Void is not an expression");
  }
}
