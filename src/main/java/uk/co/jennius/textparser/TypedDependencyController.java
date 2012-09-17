package uk.co.jennius.textparser;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TypedDependencyController extends HttpServlet {

	private static final long serialVersionUID = 6454731624143069304L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
		Gson gson = new Gson();
		String text = req.getPathInfo();
		if(text == null){
			resp.sendError(HttpServletResponse.SC_OK,"text was empty");
			return;
		}
		
		if(text.length() > 0){
			// remove trailing '/' 
			text = text.substring(1);
		}
		
		List<Sentence> sentences;
		try {
			sentences = new TypedDependencyParser().getTypedDependencies(text);
		} catch (TextParserException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,e.getMessage());
			return;
		}
			
		String json = gson.toJson(sentences, sentences.getClass());
		resp.getWriter().print(json);
    }
}
