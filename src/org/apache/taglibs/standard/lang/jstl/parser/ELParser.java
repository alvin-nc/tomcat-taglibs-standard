/* Generated By:JavaCC: Do not edit this line. ELParser.java */
package org.apache.taglibs.standard.lang.jstl.parser;

import org.apache.taglibs.standard.lang.jstl.*;
import java.util.ArrayList;
import java.util.List;

public class ELParser implements ELParserConstants {

  public static void main(String args[])
       throws ParseException
  {
    ELParser parser = new ELParser (System.in);
    parser.ExpressionString ();
  }

/*****************************************
 * GRAMMAR PRODUCTIONS *
 *****************************************/

/**
 *
 * Returns a String if the expression string is a single String, an
 * Expression if the expression string is a single Expression, an
 * ExpressionString if it's a mixture of both.
 **/
  final public Object ExpressionString() throws ParseException {
  Object ret = "";
  List elems = null;
  Object elem;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NON_EXPRESSION_TEXT:
      ret = AttrValueString();
      break;
    case START_EXPRESSION:
      ret = AttrValueExpression();
      break;
    default:
      jj_la1[0] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NON_EXPRESSION_TEXT:
      case START_EXPRESSION:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NON_EXPRESSION_TEXT:
        elem = AttrValueString();
        break;
      case START_EXPRESSION:
        elem = AttrValueExpression();
        break;
      default:
        jj_la1[2] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
       if (elems == null) {
         elems = new ArrayList ();
         elems.add (ret);
       }
       elems.add (elem);
    }
    if (elems != null) {
      ret = new ExpressionString (elems.toArray ());
    }
    {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public String AttrValueString() throws ParseException {
  Token t;
    t = jj_consume_token(NON_EXPRESSION_TEXT);
    {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  final public Expression AttrValueExpression() throws ParseException {
  Expression exp;
    jj_consume_token(START_EXPRESSION);
    exp = Expression();
    jj_consume_token(END_EXPRESSION);
     {if (true) return exp;}
    throw new Error("Missing return statement in function");
  }

  final public Expression Expression() throws ParseException {
  Expression ret;
    ret = OrExpression();
    {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public Expression OrExpression() throws ParseException {
  Expression startExpression;
  BinaryOperator operator;
  Expression expression;
  List operators = null;
  List expressions = null;
    startExpression = AndExpression();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OR1:
      case OR2:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_2;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OR1:
        jj_consume_token(OR1);
        break;
      case OR2:
        jj_consume_token(OR2);
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                        operator = OrOperator.SINGLETON;
      expression = AndExpression();
        if (operators == null) {
          operators = new ArrayList ();
          expressions = new ArrayList ();
        }
        operators.add (operator);
        expressions.add (expression);
    }
    if (operators != null) {
      {if (true) return new BinaryOperatorExpression (startExpression,
                                           operators,
                                           expressions);}
    }
    else {
      {if (true) return startExpression;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Expression AndExpression() throws ParseException {
  Expression startExpression;
  BinaryOperator operator;
  Expression expression;
  List operators = null;
  List expressions = null;
    startExpression = EqualityExpression();
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AND1:
      case AND2:
        ;
        break;
      default:
        jj_la1[5] = jj_gen;
        break label_3;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AND1:
        jj_consume_token(AND1);
        break;
      case AND2:
        jj_consume_token(AND2);
        break;
      default:
        jj_la1[6] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                          operator = AndOperator.SINGLETON;
      expression = EqualityExpression();
        if (operators == null) {
          operators = new ArrayList ();
          expressions = new ArrayList ();
        }
        operators.add (operator);
        expressions.add (expression);
    }
    if (operators != null) {
      {if (true) return new BinaryOperatorExpression (startExpression,
                                           operators,
                                           expressions);}
    }
    else {
      {if (true) return startExpression;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Expression EqualityExpression() throws ParseException {
  Expression startExpression;
  BinaryOperator operator;
  Expression expression;
  List operators = null;
  List expressions = null;
    startExpression = RelationalExpression();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EQ1:
      case EQ2:
      case NE1:
      case NE2:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_4;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EQ1:
      case EQ2:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case EQ1:
          jj_consume_token(EQ1);
          break;
        case EQ2:
          jj_consume_token(EQ2);
          break;
        default:
          jj_la1[8] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                        operator = EqualsOperator.SINGLETON;
        break;
      case NE1:
      case NE2:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NE1:
          jj_consume_token(NE1);
          break;
        case NE2:
          jj_consume_token(NE2);
          break;
        default:
          jj_la1[9] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                          operator = NotEqualsOperator.SINGLETON;
        break;
      default:
        jj_la1[10] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      expression = RelationalExpression();
        if (operators == null) {
          operators = new ArrayList ();
          expressions = new ArrayList ();
        }
        operators.add (operator);
        expressions.add (expression);
    }
    if (operators != null) {
      {if (true) return new BinaryOperatorExpression (startExpression,
                                           operators,
                                           expressions);}
    }
    else {
      {if (true) return startExpression;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Expression RelationalExpression() throws ParseException {
  Expression startExpression;
  BinaryOperator operator;
  Expression expression;
  List operators = null;
  List expressions = null;
    startExpression = AddExpression();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case GT1:
      case GT2:
      case LT1:
      case LT2:
      case LE1:
      case LE2:
      case GE1:
      case GE2:
        ;
        break;
      default:
        jj_la1[11] = jj_gen;
        break label_5;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LT1:
      case LT2:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LT1:
          jj_consume_token(LT1);
          break;
        case LT2:
          jj_consume_token(LT2);
          break;
        default:
          jj_la1[12] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                        operator = LessThanOperator.SINGLETON;
        break;
      case GT1:
      case GT2:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case GT1:
          jj_consume_token(GT1);
          break;
        case GT2:
          jj_consume_token(GT2);
          break;
        default:
          jj_la1[13] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                          operator = GreaterThanOperator.SINGLETON;
        break;
      case GE1:
      case GE2:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case GE1:
          jj_consume_token(GE1);
          break;
        case GE2:
          jj_consume_token(GE2);
          break;
        default:
          jj_la1[14] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                          operator = GreaterThanOrEqualsOperator.SINGLETON;
        break;
      case LE1:
      case LE2:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LE1:
          jj_consume_token(LE1);
          break;
        case LE2:
          jj_consume_token(LE2);
          break;
        default:
          jj_la1[15] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                          operator = LessThanOrEqualsOperator.SINGLETON;
        break;
      default:
        jj_la1[16] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      expression = AddExpression();
        if (operators == null) {
          operators = new ArrayList ();
          expressions = new ArrayList ();
        }
        operators.add (operator);
        expressions.add (expression);
    }
    if (operators != null) {
      {if (true) return new BinaryOperatorExpression (startExpression,
                                           operators,
                                           expressions);}
    }
    else {
      {if (true) return startExpression;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Expression AddExpression() throws ParseException {
  Expression startExpression;
  BinaryOperator operator;
  Expression expression;
  List operators = null;
  List expressions = null;
    startExpression = MultiplyExpression();
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case MINUS:
        ;
        break;
      default:
        jj_la1[17] = jj_gen;
        break label_6;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        jj_consume_token(PLUS);
               operator = PlusOperator.SINGLETON;
        break;
      case MINUS:
        jj_consume_token(MINUS);
                  operator = MinusOperator.SINGLETON;
        break;
      default:
        jj_la1[18] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      expression = MultiplyExpression();
        if (operators == null) {
          operators = new ArrayList ();
          expressions = new ArrayList ();
        }
        operators.add (operator);
        expressions.add (expression);
    }
    if (operators != null) {
      {if (true) return new BinaryOperatorExpression (startExpression,
                                           operators,
                                           expressions);}
    }
    else {
      {if (true) return startExpression;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Expression MultiplyExpression() throws ParseException {
  Expression startExpression;
  BinaryOperator operator;
  Expression expression;
  List operators = null;
  List expressions = null;
    startExpression = UnaryExpression();
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case MULTIPLY:
      case DIVIDE1:
      case DIVIDE2:
      case MODULUS1:
      case MODULUS2:
        ;
        break;
      default:
        jj_la1[19] = jj_gen;
        break label_7;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case MULTIPLY:
        jj_consume_token(MULTIPLY);
                   operator = MultiplyOperator.SINGLETON;
        break;
      case DIVIDE1:
      case DIVIDE2:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DIVIDE1:
          jj_consume_token(DIVIDE1);
          break;
        case DIVIDE2:
          jj_consume_token(DIVIDE2);
          break;
        default:
          jj_la1[20] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                                  operator = DivideOperator.SINGLETON;
        break;
      case MODULUS1:
      case MODULUS2:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case MODULUS1:
          jj_consume_token(MODULUS1);
          break;
        case MODULUS2:
          jj_consume_token(MODULUS2);
          break;
        default:
          jj_la1[21] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                                    operator = ModulusOperator.SINGLETON;
        break;
      default:
        jj_la1[22] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      expression = UnaryExpression();
        if (operators == null) {
          operators = new ArrayList ();
          expressions = new ArrayList ();
        }
        operators.add (operator);
        expressions.add (expression);
    }
    if (operators != null) {
      {if (true) return new BinaryOperatorExpression (startExpression,
                                           operators,
                                           expressions);}
    }
    else {
      {if (true) return startExpression;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Expression UnaryExpression() throws ParseException {
  Expression expression;
  UnaryOperator singleOperator = null;
  UnaryOperator operator;
  List operators = null;
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case MINUS:
      case NOT1:
      case NOT2:
      case EMPTY:
        ;
        break;
      default:
        jj_la1[23] = jj_gen;
        break label_8;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NOT1:
      case NOT2:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NOT1:
          jj_consume_token(NOT1);
          break;
        case NOT2:
          jj_consume_token(NOT2);
          break;
        default:
          jj_la1[24] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
                        operator = NotOperator.SINGLETON;
        break;
      case MINUS:
        jj_consume_token(MINUS);
                operator = UnaryMinusOperator.SINGLETON;
        break;
      case EMPTY:
        jj_consume_token(EMPTY);
                operator = EmptyOperator.SINGLETON;
        break;
      default:
        jj_la1[25] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    if (singleOperator == null) {
      singleOperator = operator;
    }
    else if (operators == null) {
      operators = new ArrayList ();
      operators.add (singleOperator);
      operators.add (operator);
    }
    else {
      operators.add (operator);
    }
    }
    expression = Value();
    if (operators != null) {
      {if (true) return new UnaryOperatorExpression (null, operators, expression);}
    }
    else if (singleOperator != null) {
      {if (true) return new UnaryOperatorExpression (singleOperator, null, expression);}
    }
    else {
      {if (true) return expression;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Expression Value() throws ParseException {
  Expression prefix;
  ValueSuffix suffix;
  List suffixes = null;
    prefix = ValuePrefix();
    label_9:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DOT:
      case LBRACKET:
        ;
        break;
      default:
        jj_la1[26] = jj_gen;
        break label_9;
      }
      suffix = ValueSuffix();
        if (suffixes == null) {
          suffixes = new ArrayList ();
        }
        suffixes.add (suffix);
    }
    if (suffixes == null) {
      {if (true) return prefix;}
    }
    else {
      {if (true) return new ComplexValue (prefix, suffixes);}
    }
    throw new Error("Missing return statement in function");
  }

/**
 *
 * This is an element that can start a value
 **/
  final public Expression ValuePrefix() throws ParseException {
  Expression ret;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case STRING_LITERAL:
    case TRUE:
    case FALSE:
    case NULL:
      ret = Literal();
      break;
    case LPAREN:
      jj_consume_token(LPAREN);
      ret = Expression();
      jj_consume_token(RPAREN);
      break;
    case IDENTIFIER:
      ret = NamedValue();
      break;
    default:
      jj_la1[27] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
      {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public NamedValue NamedValue() throws ParseException {
  Token t;
    t = jj_consume_token(IDENTIFIER);
                     {if (true) return new NamedValue (t.image);}
    throw new Error("Missing return statement in function");
  }

  final public ValueSuffix ValueSuffix() throws ParseException {
  ValueSuffix suffix;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case DOT:
      suffix = PropertySuffix();
      break;
    case LBRACKET:
      suffix = ArraySuffix();
      break;
    default:
      jj_la1[28] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
      {if (true) return suffix;}
    throw new Error("Missing return statement in function");
  }

  final public PropertySuffix PropertySuffix() throws ParseException {
  Token t;
  String property;
    jj_consume_token(DOT);
    property = Identifier();
      {if (true) return new PropertySuffix (property);}
    throw new Error("Missing return statement in function");
  }

  final public ArraySuffix ArraySuffix() throws ParseException {
  Expression index;
    jj_consume_token(LBRACKET);
    index = Expression();
    jj_consume_token(RBRACKET);
     {if (true) return new ArraySuffix (index);}
    throw new Error("Missing return statement in function");
  }

  final public Literal Literal() throws ParseException {
  Literal ret;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TRUE:
    case FALSE:
      ret = BooleanLiteral();
      break;
    case INTEGER_LITERAL:
      ret = IntegerLiteral();
      break;
    case FLOATING_POINT_LITERAL:
      ret = FloatingPointLiteral();
      break;
    case STRING_LITERAL:
      ret = StringLiteral();
      break;
    case NULL:
      ret = NullLiteral();
      break;
    default:
      jj_la1[29] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
      {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public BooleanLiteral BooleanLiteral() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TRUE:
      jj_consume_token(TRUE);
           {if (true) return BooleanLiteral.TRUE;}
      break;
    case FALSE:
      jj_consume_token(FALSE);
              {if (true) return BooleanLiteral.FALSE;}
      break;
    default:
      jj_la1[30] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public StringLiteral StringLiteral() throws ParseException {
  Token t;
    t = jj_consume_token(STRING_LITERAL);
    {if (true) return StringLiteral.fromToken (t.image);}
    throw new Error("Missing return statement in function");
  }

  final public IntegerLiteral IntegerLiteral() throws ParseException {
  Token t;
    t = jj_consume_token(INTEGER_LITERAL);
    {if (true) return new IntegerLiteral (t.image);}
    throw new Error("Missing return statement in function");
  }

  final public FloatingPointLiteral FloatingPointLiteral() throws ParseException {
  Token t;
    t = jj_consume_token(FLOATING_POINT_LITERAL);
    {if (true) return new FloatingPointLiteral (t.image);}
    throw new Error("Missing return statement in function");
  }

  final public NullLiteral NullLiteral() throws ParseException {
    jj_consume_token(NULL);
      {if (true) return NullLiteral.SINGLETON;}
    throw new Error("Missing return statement in function");
  }

  final public String Identifier() throws ParseException {
  Token t;
    t = jj_consume_token(IDENTIFIER);
      {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  public ELParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[31];
  final private int[] jj_la1_0 = {0x6,0x6,0x6,0x0,0x0,0x0,0x0,0x18600000,0x600000,0x18000000,0x18600000,0x79e0000,0x180000,0x60000,0x6000000,0x1800000,0x79e0000,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x80010000,0x20007580,0x80010000,0x7580,0x3000,};
  final private int[] jj_la1_1 = {0x0,0x0,0x0,0x3000,0x3000,0xc00,0xc00,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x6,0x6,0xf8,0x30,0xc0,0xf8,0x4304,0x300,0x4304,0x0,0x8000,0x0,0x0,0x0,};

  public ELParser(java.io.InputStream stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ELParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
  }

  public ELParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ELParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
  }

  public ELParser(ELParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
  }

  public void ReInit(ELParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;

  final public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[52];
    for (int i = 0; i < 52; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 31; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 52; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

}
