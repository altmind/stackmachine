package us.altio.sm;

import gnu.trove.stack.TLongStack;
import gnu.trove.stack.array.TLongArrayStack;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;

import us.altio.sm.typedef.ProgramCode;
import us.altio.sm.typedef.ProgramContext;
import us.altio.sm.util.Enforcer;
import us.altio.sm.util.Pair;

/**
 * State Machine as specified on
 * http://iit.bsuir.by/mediawiki/index.php/%D0%A3%D0
 * %9F:%D0%9F%D0%9F%D0%B2%D0%98%D0%A1_3%D1%81%D0%B5%D0%BC_%D0%9B%D0%A03
 */
public class StackMachine {

	private long IP;
	private long SP;
	private ProgramContext valuesMap;
	private ProgramCode code;
	private ProgramLoader programLoader;
	private TLongStack programStack;

	private final static Enforcer<IllegalStateException> stateEnforcer = new Enforcer<IllegalStateException>();

	StackMachine() {
		programLoader = new ProgramLoader(this);
	}

	public ProgramLoader getProgramLoader() {
		return programLoader;
	}

	public void execute(long entryPoint) {
		Pair<ProgramCode, ProgramContext> pair = programLoader.getContext();
		code = pair.getField1();
		valuesMap = pair.getField2();
		valuesMap.put("print$num", -1L);
		valuesMap.put("print$char", -1L);
		valuesMap.put("println$num", -1L);
		valuesMap.put("println$char", -1L);
		IP = entryPoint;
		programStack = new TLongArrayStack(5000);
		while (true) {
			Operation currentOperation = code.get((int) IP);
			if (!executeOperation(currentOperation)) {
				return;
			}
		}
	}

	private long pop() {
		stateEnforcer.enforce(SP >= 1);
		SP--;
		return programStack.pop();
	}

	private long push(long val) {
		programStack.push(val);
		SP++;
		return val;
	}

	private boolean executeOperation(Operation currentOperation) {
		if (IP < 0)
			return false;
		switch (currentOperation.op) {
		case ADD:
			push(pop() + pop());
			IP++;
			break;
		case SUB:
			push(pop() - pop());
			IP++;
			break;
		case DIV:
			stateEnforcer.enforce(SP >= 2);
			push((long) (pop() / pop()));
			IP++;
			break;
		case MUL:
			stateEnforcer.enforce(SP >= 2);
			push((long) (pop() + pop()));
			IP++;
			break;
		case GOTO:
			stateEnforcer.enforce(StringUtils
					.isNotBlank(currentOperation.operand));
			if (isLong(currentOperation.operand))
				IP = Long.parseLong(currentOperation.operand);
			else
				IP = valuesMap.get(currentOperation.operand);
			break;
		case CALL:
			stateEnforcer.enforce(StringUtils
					.isNotBlank(currentOperation.operand));
			long candidateIP = valuesMap.get(currentOperation.operand);
			if (candidateIP < 0) {
				evaluateBuiltinByName(currentOperation.operand);
				IP++;
			} else {
				push(IP);
				IP = candidateIP;
			}
			break;
		case DUP:
			push(push(pop()));
			IP++;
			break;
		case IFEQ:
			stateEnforcer.enforce(StringUtils
					.isNotBlank(currentOperation.operand));
			if (pop() == pop())
				if (isLong(currentOperation.operand))
					IP = Long.parseLong(currentOperation.operand);
				else
					IP = valuesMap.get(currentOperation.operand);
			else
				IP++;
			break;
		case IFGR:
			stateEnforcer.enforce(StringUtils
					.isNotBlank(currentOperation.operand));
			// i'm unsure about order of evaluation in 1 statement. do in seq.
			long upper = pop();
			long lower = pop();
			if (upper > lower)
				if (isLong(currentOperation.operand))
					IP = Long.parseLong(currentOperation.operand);
				else
					IP = valuesMap.get(currentOperation.operand);
			else
				IP++;
			break;
		case POP:
			stateEnforcer.enforce(StringUtils
					.isNotBlank(currentOperation.operand));
			valuesMap.put(currentOperation.operand, pop());
			IP++;
			break;
		case PUSH:
			stateEnforcer.enforce(StringUtils
					.isNotBlank(currentOperation.operand));
			if (isLong(currentOperation.operand))
				push(Long.parseLong(currentOperation.operand));
			else
				push(valuesMap.get(currentOperation.operand));
			IP++;
			break;
		default:
			throw new IllegalStateException(MessageFormat.format(
					"Unhandled opcode found: \"{0}\"", currentOperation.op));
		}
		return true;

	}

	private void evaluateBuiltinByName(String name) {
		if (name.equals("print$num")) {
			System.out.print(pop());
		} else if (name.equals("print$char")) {
			System.out.print((char) pop());
		} else if (name.equals("println$char")) {
			System.out.println((char) pop());
		} else if (name.equals("println$num")) {
			System.out.println(pop());
		}
		else throw new IllegalArgumentException(MessageFormat.format("Cannot find handler for build-in function \"{}\"", name));
	}

	public boolean isLong(String i) {
		try {
			Long.parseLong(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

}
