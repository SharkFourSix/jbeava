package lib.gintec_rdl.jbeava.validation.utils;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.List;

public class StringTemplateEngineTest extends TestCase {

    public void testRender() {
        StringTemplateEngine engine;
        String result;
        String input;
        String expected;
        HashMap<String, Object> model;

        input = "John Doe";
        expected = "Hello John Doe. You are a 25 year old male.";
        engine = new StringTemplateEngine();
        model = new HashMap<>();

        model.put("name", input);
        model.put("age", 25);
        model.put("gender", "male");

        result = engine.render("Hello ${name}. You are a ${age} year old ${gender}.", model);
        assertEquals("Rendered string does not match expected output", expected, result);
    }

    public void testTokenization(){
        String template;
        List<StringTemplateEngine.TokenGroup> tokenGroups;
        StringTemplateEngine engine;

        template = "$P ${var1} $$$${var2} ${var3$Helloworld$} ${_Var4____0_________$}";
        engine = new StringTemplateEngine();
        tokenGroups = engine.tokenize(template);
        assertEquals(8, tokenGroups.size());

        System.out.println(tokenGroups);
    }
}