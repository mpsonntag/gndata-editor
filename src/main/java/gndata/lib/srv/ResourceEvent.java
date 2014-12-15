package gndata.lib.srv;


import java.time.LocalDateTime;
import java.util.Calendar;

import com.hp.hpl.jena.rdf.model.Resource;
import gndata.lib.util.Resources;


public class ResourceEvent implements EventAdapter {

    private Resource res;

    public ResourceEvent(Resource res) {
        this.res = res;
    }

    public LocalDateTime getEventStart() {
        return LocalDateTime.of(Calendar.YEAR,Calendar.MONTH,
                Calendar.DAY_OF_MONTH, 8,15, 0, 0);
    }

    public LocalDateTime getEventEnd() {
        return LocalDateTime.of(Calendar.YEAR, Calendar.MONTH,
                Calendar.DAY_OF_MONTH, 16, 15, 0, 0);
    }

    public String getSummary() {
        return Resources.toNameString(res);
    }

    public String getDescription() {
        return Resources.toInfoString(res);
    }


}
