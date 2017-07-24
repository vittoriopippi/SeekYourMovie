package spotlight.util;


import org.restlet.resource.ClientResource;

public class ToClient {
	public synchronized static String Response(String link){
		String response = null;
		
		try{
			response = new ClientResource(link).get().getText();
		}
		catch(Exception e){
			try {
				Thread.sleep(2000);
				response = new ClientResource(link).get().getText();
			} catch (Exception e1) {
				return null;
			}
		}

		return response;
	}
}
