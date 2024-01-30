package activityrecord.web.view;


import java.util.logging.Level;
import java.util.logging.Logger;

import activityrecord.data.GraphDataTO;
import activityrecord.data.RactDAO;
import activityrecord.graph.PeriodSummaryGraph;
import activityrecord.graph.WeekSummaryGraph;
import activityrecord.ract.ActivityReportResource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import common.web.ClientInfo;
import common.web.view.*;
import gausssoft.drawing.Canvas;
import gausssoft.graph.Graph;
import siscorp.framework.application.ApplicationEvent;
import siscorp.framework.application.EventResponse;
import siscorp.framework.application.Session;
import siscorp.framework.application.web.WebApplicationConstants;


public class ImageView extends GenericHtmlView
{
	public ImageView()
	{
		super();
	}

	@Override
	public void render(ApplicationEvent event, EventResponse response) throws Exception
	{
		Session session;
		HttpServletResponse httpResponse;
		ClientInfo clientInfo;
		ServletOutputStream stream;
        Graph graph;
        ActivityReportResource resource = ( ActivityReportResource ) event.getRequest().getSession().getAttribute( "RESOURCE" );
        GraphDataTO transferObject;  
        long startTime = System.nanoTime();
        Logger logger = Logger.getLogger( "siscorp.registroactividades" );
        logger.log( Level.INFO, "**** > render ImageView "+resource.getResourceCode()+" : "+RactDAO.getCurrentDateAndTime() );

		
		session = event.getRequest().getSession();
		
		httpResponse = (HttpServletResponse) event.getRequest().
			getAttribute( WebApplicationConstants.HTTP_RESPONSE_KEY );
											
		// httpResponse.addHeader("Cache-Control","no-cache");
		httpResponse.addHeader("Cache-Control","no-store");
		httpResponse.setContentType( "image/*" );
		
		clientInfo = (ClientInfo) session.getAttribute("CLIENTINFO");
		clientInfo.checkDirectory();
		
		stream = httpResponse.getOutputStream();

        transferObject = (GraphDataTO) session.getAttribute("GRAPHINFO");
        if (transferObject.isWeeklyGraph )
        {
            graph = new WeekSummaryGraph( transferObject.week,
                                          transferObject.dimension,
                                          transferObject.userSpace,
                                          transferObject.dateDataSerie,
                                          transferObject.numericDataSerie,
                                          transferObject.minDate,
                                          transferObject.maxDate );
        } 
        else
        {
            graph = new PeriodSummaryGraph( transferObject.currentPeriod,
                                            transferObject.dimension,
                                            transferObject.userSpace,
                                            transferObject.summary,
                                            transferObject.minDate,
                                            transferObject.maxDate,
                                            transferObject.bigCurrentPeriod,
                                            transferObject.locale );
        }
	Canvas canvas = new Canvas();
        canvas.setDefaultTransform( transferObject.userSpace );
        canvas.addObject( graph );
        canvas.setSize( transferObject.dimension );
        canvas.export(stream);
	stream.flush();
	long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        logger.log( Level.INFO,"render ImageView "+resource.getResourceCode()+" DURATION:"+ duration/1_000_000_000);
        logger.log( Level.INFO,"*****");
	logger.log( Level.INFO, "**** < render ImageView "+resource.getResourceCode()+" "+RactDAO.getCurrentDateAndTime() );
	}	
}
