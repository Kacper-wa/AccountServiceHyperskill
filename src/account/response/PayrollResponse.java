package account.response;

import account.entity.Payroll;

import java.util.ArrayList;
import java.util.List;

public record PayrollResponse(
        String name,
        String lastname,
        String period,
        String salary
){
    public static PayrollResponse response(Payroll payroll) {
        String name = payroll.getUser().getName();
        String lastname = payroll.getUser().getLastname();
        String period = payroll.getPeriod();
        Long salary = payroll.getSalary();
        return new PayrollResponse(
                name,
                lastname,
                getFormattedPeriod(period),
                getFormattedSalary(salary));
    }

    public static List<PayrollResponse> response(List<Payroll> payrolls) {
        List<PayrollResponse> payrollResponses = new ArrayList<>();
        for (Payroll payroll : payrolls) {
            String name = payroll.getUser().getName();
            String lastname = payroll.getUser().getLastname();
            String period = payroll.getPeriod();
            Long salary = payroll.getSalary();
            payrollResponses.add(new PayrollResponse(
                    name,
                    lastname,
                    getFormattedPeriod(period),
                    getFormattedSalary(salary)));
        }
        return payrollResponses;
    }

    public static String getFormattedPeriod(String period) {
        switch (period) {
            case "01-2021" -> {
                return "January-2021";
            }
            case "02-2021" -> {
                return "February-2021";
            }
            case "03-2021" -> {
                return "March-2021";
            }
            case "04-2021" -> {
                return "April-2021";
            }
            case "05-2021" -> {
                return "May-2021";
            }
            case "06-2021" -> {
                return "June-2021";
            }
            case "07-2021" -> {
                return "July-2021";
            }
            case "08-2021" -> {
                return "August-2021";
            }
            case "09-2021" -> {
                return "September-2021";
            }
            case "10-2021" -> {
                return "October-2021";
            }
            case "11-2021" -> {
                return "November-2021";
            }
            case "12-2021" -> {
                return "December-2021";
            }
        }
        return period;
    }

    public static String getFormattedSalary(Long salary) {
        return String.format("%d dollar(s) %d cent(s)", salary / 100, salary % 100);
    }
}
