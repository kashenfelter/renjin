package org.renjin.compiler.pipeline.fusion.kernel;

import org.renjin.compiler.pipeline.ComputeMethod;
import org.renjin.compiler.pipeline.fusion.node.LoopNode;
import org.renjin.repackaged.asm.Label;
import org.renjin.repackaged.asm.MethodVisitor;
import org.renjin.repackaged.guava.base.Optional;

import static org.renjin.repackaged.asm.Opcodes.*;


public class ColSumKernel implements LoopKernel {

  @Override
  public void compute(ComputeMethod method, LoopNode operands[]) {

    MethodVisitor mv = method.getVisitor();

    LoopNode matrix = operands[0];
    matrix.init(method);

    LoopNode numColumnsAccessor = operands[1];
    numColumnsAccessor.init(method);

    int numColumns = method.reserveLocal(1);

    // create the array to hold the sums
    int resultArray = method.reserveLocal(1);
    numColumnsAccessor.pushElementAsInt(method, 0);
    mv.visitInsn(DUP);
    mv.visitVarInsn(ISTORE, numColumns);
    mv.visitIntInsn(NEWARRAY, T_DOUBLE);
    mv.visitVarInsn(ASTORE, resultArray);

    // numRows = length / numColumns
    int numRows = method.reserveLocal(1);
    matrix.pushLength(method);
    mv.visitVarInsn(ILOAD, numColumns);
    mv.visitInsn(IDIV);
    mv.visitVarInsn(ISTORE, numRows);

    // initialize our counters
    int colIndex = method.declareCounter();
    int rowIndex = method.declareCounter();
    int sourceIndex = method.declareCounter();

    int sum = method.reserveLocal(2);
    mv.visitInsn(DCONST_0);
    mv.visitVarInsn(DSTORE, sum);

    Label loopHead = new Label();
    Label nextElement = new Label();

    Optional<Label> integerNaLabel;
    if(matrix.mustCheckForIntegerNAs()) {
      integerNaLabel = Optional.of(new Label());
    } else {
      integerNaLabel = Optional.absent();
    }

    // START THE LOOP!!
    mv.visitLabel(loopHead);

    // load the next element onto the stack
    mv.visitVarInsn(ILOAD, sourceIndex);
    matrix.pushElementAsDouble(method, integerNaLabel);


    // load the current sum on the stack, add this value to it,
    // and then store back to the local variable slot
    mv.visitVarInsn(DLOAD, sum);
    mv.visitInsn(DADD);
    mv.visitVarInsn(DSTORE, sum);

    mv.visitJumpInsn(GOTO, nextElement);

    // HANDLE THE INTEGER NA CASE:
    if(integerNaLabel.isPresent()) {
      mv.visitLabel(integerNaLabel.get());
      // discard the NA value on the stack
      mv.visitInsn(POP);
    }

    // GO TO NEXT ELEMENT
    mv.visitLabel(nextElement);

    // increment sourceIndex and rowIndex
    mv.visitIincInsn(sourceIndex, 1);
    mv.visitIincInsn(rowIndex, 1);

    // if we haven't reached the end of the column (rowIndex == numRows),
    // add the next element to the sum
    mv.visitVarInsn(ILOAD, rowIndex);
    mv.visitVarInsn(ILOAD, numRows);
    mv.visitJumpInsn(IF_ICMPNE, loopHead);

    // otherwise, add to the next row
    // save the sum to the result array
    mv.visitVarInsn(ALOAD, resultArray);
    mv.visitVarInsn(ILOAD, colIndex);
    mv.visitVarInsn(DLOAD, sum);
    mv.visitInsn(DASTORE);

    // reset row index
    mv.visitInsn(ICONST_0);
    mv.visitVarInsn(ISTORE, rowIndex);

    // reset sum
    mv.visitInsn(DCONST_0);
    mv.visitVarInsn(DSTORE, sum);

    // increment column index
    mv.visitIincInsn(colIndex, 1);

    // .. and check to see if we have any columns left
    mv.visitVarInsn(ILOAD, colIndex);
    mv.visitVarInsn(ILOAD, numColumns);

    mv.visitJumpInsn(IF_ICMPNE, loopHead);

    // otherwise, we're done!
    mv.visitVarInsn(ALOAD, resultArray);
    mv.visitInsn(ARETURN);

  }

  @Override
  public String debugLabel(LoopNode[] operands) {
    return "colSums(" + operands[0] + ")";
  }
}
