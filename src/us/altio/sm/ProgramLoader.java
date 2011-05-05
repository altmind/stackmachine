package us.altio.sm;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import us.altio.sm.Operation.OperationEnum;
import us.altio.sm.typedef.ProgramCode;
import us.altio.sm.typedef.ProgramContext;
import us.altio.sm.util.Pair;

public class ProgramLoader {

	private static final GrammarRules grammar = new GrammarRules();
	private long currentPosition;
	private StackMachine sm;
	private ProgramCode listing;
	private ProgramContext labelMap;
	private boolean LoaderShutdowned = false;

	ProgramLoader(StackMachine sm) {
		this.sm = sm;
		currentPosition = 0;
		listing = new ProgramCode();
		labelMap = new ProgramContext();
	}

	public void addLine(String line) {
		if (LoaderShutdowned)
			throw new IllegalStateException(
					"Cannot add any operations after program started executing");
		Matcher matcher = grammar.lineRule.matcher(line);
		if (matcher.find()) {
			String label = "";
			String op;
			String operand = "";

			if (StringUtils.isNotBlank(matcher.group(1)))
				label = StringUtils.chop(matcher.group(1));
			op = matcher.group(2);
			if (StringUtils.isNotBlank(matcher.group(3)))
				operand = StringUtils.trim(matcher.group(3));

			addNewOperation(label, op, operand);
			
		} else {
			throw new IllegalArgumentException(MessageFormat.format(
					"Incorrect syntax while matching: \"{0}\"", line));
		}
	}

	private void addNewOperation(String label, String op, String operand) {
		if (!label.equals(StringUtils.EMPTY)) {
			labelMap.put(label, currentPosition);
		}
		OperationEnum o = Operation.OperationEnum.valueOfLowerCase(op);
		if (o.equals(OperationEnum.FUNCTION)) {
			labelMap.put(operand, currentPosition);
			return;
		}
		listing.add(new Operation(o, operand));
		currentPosition++;
	}

	public Pair<ProgramCode, ProgramContext> getContext() {
		LoaderShutdowned = true;
		return new Pair<ProgramCode, ProgramContext>(listing, labelMap);
	}

}
