package us.altio.sm;

import junit.framework.TestCase;

public class ProgramLoaderTest extends TestCase {

	public void testAddLine() {
		StackMachine sm = new StackMachine();
		l(sm,"push 0");
		l(sm,"function f");
		l(sm,"push 1");
		l(sm,"add");
		l(sm,"dup");
		l(sm,"call println$num");
		l(sm,"goto f");
		/*l(sm, "function main		");
		l(sm, "	push 3			");
		l(sm, "	call factorial		");
		l(sm, "	call print$num	");
		l(sm, "	goto 0			");
		l(sm, "function factorial	");
		l(sm, "	pop ret			");
		l(sm, "	dup				");
		l(sm, "	push 1			");
		l(sm, "	ifeq lr				");
		l(sm, "	dup				");
		l(sm, "	push 1			");
		l(sm, "	sub				");
		l(sm, "	call factorial		");
		l(sm, "	mul				");
		l(sm, "lr:	goto ret			");*/
		sm.execute(0);
	}

	void l(StackMachine sm, String s) {
		sm.getProgramLoader().addLine(s);
	};

}
