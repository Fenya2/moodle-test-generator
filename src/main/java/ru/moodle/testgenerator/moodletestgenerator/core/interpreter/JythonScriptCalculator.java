package ru.moodle.testgenerator.moodletestgenerator.core.interpreter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.python.util.PythonInterpreter;

import com.google.inject.Singleton;

/**
 * Вычисляет зависимые параметры с использованием библиотеки jython
 *
 * @author dsyromyatnikov
 * @since 11.10.2025
 */
@Singleton
public class JythonScriptCalculator implements ScriptCalculator
{
    /**
     * Скрипт, запускаемый сразу после старта интерпретатора
     */
    private static final String IMPORTING = """
            from decimal import Decimal
            import math
            import random
            """;
    private static final String FOUR_SPACES = "    ";

    private static final String EXECUTION_SCRIPT_TEMPLATE = """                 
            #Функция от именованных параметров с заданными значениями по умолчанию
            def generated_function(%s):
            %s
            
            # Вызов функции с Decimal параметрами по умолчанию
            result = str(generated_function())
            """;

    /**
     * Переменная, в которой находится результат последнего вычисленного в интерпретаторе скрипта
     */
    private static final String RESULT = "result";
    private final PythonInterpreter interpreter;
    private final Lock lock;

    public JythonScriptCalculator()
    {
        this.lock = new ReentrantLock();
        try
        {
            lock.lock();
            this.interpreter = new PythonInterpreter();
            interpreter.exec(IMPORTING);
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public BigDecimal calculateScript(String calculationScript, Map<String, BigDecimal> dependenciesValues)
    {
        String script = generateCalculationScript(calculationScript, dependenciesValues);
        try
        {
            lock.lock();
            interpreter.exec(script);
            return new BigDecimal(interpreter.get(RESULT).asString());
        }
        finally
        {
            lock.unlock();
        }
    }

    private static String generateCalculationScript(String calculationScript,
            Map<String, BigDecimal> dependenciesValues)
    {
        String functionParameters = dependenciesValues.entrySet().stream()
                .map(entry -> "%s=Decimal('%s')".formatted(entry.getKey(), entry.getValue().toPlainString()))
                .collect(Collectors.joining(","));
        String functionBody = FOUR_SPACES + calculationScript.replace("\n", "\n" + FOUR_SPACES);
        return EXECUTION_SCRIPT_TEMPLATE.formatted(functionParameters, functionBody);
    }
}