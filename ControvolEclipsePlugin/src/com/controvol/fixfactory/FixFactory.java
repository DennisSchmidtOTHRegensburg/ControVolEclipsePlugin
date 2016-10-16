package com.controvol.fixfactory;

import java.util.List;

import com.controvol.quickfix.Fix;

import ie.ucd.pel.datastructure.warning.IWarning;

public interface FixFactory {

public List<Fix> getCodeFixes(IWarning warning);
}
