Stack Machine

commands:
	push <int|label> - push <1> on stack
	pop <var> - take arg from stack, save to <1>
	dup - take arg from stack, push S[0] and S[0] on stack
	add - take 2 args from stack, push S[0]+S[1] on stack
	sub - take 2 args from stack, push S[0]-S[1] on stack
	div - take 2 args from stack, push floor(S[0]/S[1]) on stack
	mul - take 2 args from stack, push S[0]*S[1] on stack
	goto <int|label> - jump to <1>
	ifeq <int|label> - take 2 args from stack, compare them, if 1==2, jump to <1>
	ifgr <int|label> - take 2 args from stack, compare them, if 1>2, jump to <1>
	call <int|name|label> - call predefined procedure or go to 
	function <name> - define label <1>
	<name>: - define label <1>

if Instruction counter reaches zero(e.g. goto 0), programm successfully terminates.
list of built-in routines is hard-coded in source and concludes:
	print$num
	println$num
	print$char
	println$char
stack is limited to 5000 immediate values;
all operations are performed against 4-bytes int.

LIB REQUIREMENTS:
  trove
  apache-commons
  junit
