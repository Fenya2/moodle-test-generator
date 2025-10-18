package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

/**
 * Исключение, возникающее при наличии на форме зависимого параметра, который невозможно вычислить, так как не
 * определены параметры, от которых он вычисляется (нет пути до терминальных параметров)
 */
public class UncomputableDependentParameterException extends ParameterValidationException {
    public UncomputableDependentParameterException(String message) {
        super(message);
    }
}
