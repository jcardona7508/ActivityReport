package com.gausssoft.commands;

import gausssoft.graph.Graph;

import activityrecord.data.GraphDataTO;
import activityrecord.graph.PeriodSummaryGraph;
import activityrecord.graph.WeekSummaryGraph;

import com.gausssoft.GausssoftException;
import com.gausssoft.application.ICommand;
import com.gausssoft.application.IRequestContext;
import com.gausssoft.application.IResponseContext;
import com.gausssoft.application.ResponseContext;
import com.gausssoft.application.ResultCode;
import com.gausssoft.client.MessageTO;
import com.gausssoft.service.SessionFacade;
import com.gausssoft.services.ServiceException;
import com.gausssoft.services.SessionFactory;

//Returns the level 1 of activities of Resources
/**
 * 
 * @author super
 *
 */
public class GetImage implements ICommand
{
	/**
     * Contains the name of this Command.
     */
    private String name;
    
    /**
     * 
     */
    public GetImage()
    {
    	this.setName( "GetImage" );
    }
    
	@Override
    public IResponseContext execute( IRequestContext request )
    {
	    String sessionId, usercode, resourcecode, date;
	    IResponseContext result;
	    SessionFacade session;	    		
	    MessageTO message;
	    GraphDataTO transferObject;
	    Graph graph;	    
	    String screenwidth, screenheight;
		
	    result = new ResponseContext();
	    message = new MessageTO();
	    try
            {
		sessionId = request.getSessionID();
            	session = ( SessionFacade  ) SessionFactory.getInstance().getSession(
            	        				SessionFacade.NAME, sessionId );            	    
            	resourcecode = ( String ) request.getParameter( "resourcecode" );
            	usercode = ( String ) request.getParameter( "usercode" );
            	date = ( String ) request.getParameter( "date" );
            	screenwidth = ( String ) request.getParameter( "screenwidth" );
            	screenheight = ( String ) request.getParameter( "screenheight" );
            	
        	result.setResultCode( ResultCode.FAIL );
        	    
        	if(session.validateSessionUser( usercode) )
        	{
        	    transferObject = session.getImage( resourcecode, date, screenwidth, screenheight );
        	    if (transferObject.isWeeklyGraph )
        	        {
        	            graph = new WeekSummaryGraph( transferObject.week,
        	                                          transferObject.dimension,
        	                                          transferObject.userSpace,
        	                                          transferObject.dateDataSerie,
        	                                          transferObject.numericDataSerie,
        	                                          transferObject.minDate,
        	                                          transferObject.maxDate );
        	        } else
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
		    result.setValue( "graph", graph );
        	    result.setValue( "transferObject", transferObject );
           	    result.setResultCode( ResultCode.SUCCESS );       	    
        	    
        	}
        	else
        	    result.setValue( IResponseContext.MESSAGE_KEY, message.client="sessionexpired");
            }
            catch ( ServiceException | GausssoftException e )
            {
            	result.setValue( IResponseContext.MESSAGE_KEY, message.client=e.getMessage() ); 
            	result.setExceptionMessage( e.getMessage() );
                result.setResultCode( ResultCode.EXCEPTION );
            }
    	return result;	
    }

	@Override
    public void setName( String name )
    {
	this.name = name;
    }

	@Override
    public String getName()
    {
	return this.name;
    }
	
	@Override
    public void dispose()
    {
	throw new UnsupportedOperationException( "Not supported yet." );	    
    }
}