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
		String[] text = req.getParameterValues("text");	

		if(text == null || text.length != 1){
			resp.sendError(HttpServletResponse.SC_OK,"text not found. There must be exactly one query parameter 'text' (e.g.: /typed_dependencies.json?text=Hello%20World)");
			return;
		}
	
		List<Sentence> sentences;
		try {
			sentences = new TypedDependencyParser().getTypedDependencies(text[0]);
		} catch (TextParserException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,e.getMessage());
			return;
		}
			
		String json = gson.toJson(sentences, sentences.getClass());
		resp.getWriter().print(json);
    }
}
