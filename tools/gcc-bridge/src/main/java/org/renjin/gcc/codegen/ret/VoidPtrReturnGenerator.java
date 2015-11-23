package org.renjin.gcc.codegen.ret;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.renjin.gcc.codegen.call.CallGenerator;
import org.renjin.gcc.codegen.expr.AbstractExprGenerator;
import org.renjin.gcc.codegen.expr.ExprGenerator;
import org.renjin.gcc.gimple.type.GimplePointerType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.GimpleVoidType;
import org.renjin.gcc.runtime.Ptr;

import java.util.List;

/**
 * Created by alex on 23-11-15.
 */
public class VoidPtrReturnGenerator implements ReturnGenerator {
  @Override
  public Type getType() {
    return Type.getType(Ptr.class);
  }

  @Override
  public GimpleType getGimpleType() {
    return new GimplePointerType(new GimpleVoidType());
  }

  @Override
  public void emitReturn(MethodVisitor mv, ExprGenerator valueGenerator) {
    valueGenerator.emitPushPointerWrapper(mv);
    mv.visitInsn(Opcodes.ARETURN);
  }

  @Override
  public void emitVoidReturn(MethodVisitor mv) {
    mv.visitInsn(Opcodes.ACONST_NULL);
    mv.visitInsn(Opcodes.ARETURN);
  }

  @Override
  public ExprGenerator callExpression(CallGenerator callGenerator, List<ExprGenerator> arguments) {
    return new ReturnExpr(callGenerator, arguments);
  }

  private class ReturnExpr extends AbstractExprGenerator {
    private CallGenerator callGenerator;

    public ReturnExpr(CallGenerator callGenerator, List<ExprGenerator> arguments) {
      this.callGenerator = callGenerator;
    }

    @Override
    public GimpleType getGimpleType() {
      return VoidPtrReturnGenerator.this.getGimpleType();
    }

    @Override
    public void emitPushPtrArrayAndOffset(MethodVisitor mv) {
      throw new UnsupportedOperationException();
    }
  }
}
