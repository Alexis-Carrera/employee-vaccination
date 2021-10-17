package ec.alexis.kruger.employeevaccination.swagger;

import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class RFC3339DateFormat extends StdDateFormat {

    private static final long serialVersionUID = 1L;

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {

        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df2.setTimeZone(TimeZone.getTimeZone("GMT"));
        String value = df2.format(date);
        toAppendTo.append(value);
        return toAppendTo;
    }

}
