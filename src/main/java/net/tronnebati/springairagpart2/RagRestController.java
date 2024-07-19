package net.tronnebati.springairagpart2;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RagRestController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/prompts/prompt.st")
    private Resource promptResource;

    public RagRestController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .build();
    }


    @PostMapping("/ask")
    public String ask(String question) {
        /*
        PromptTemplate promptTemplate = new PromptTemplate(promptResource);
        //chercher la reponse dans les 4 DOCUMENTS
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.query(question).withTopK(4)
        );
        //int page = (Integer) documents.get(0).getMetadata().get("page_number");
        //convertir la liste des documents en spring
        //pour chaque document je recupere contenu
        List<String> context =  documents.stream().map(d -> d.getContent()).toList();
        //creer un prompt
        Prompt prompt =  promptTemplate.create(Map.of("context",context,"question",question));
        //apres la premiere execution enlever ca et remplacer avec les trois ligne
       */
        String content = chatClient
                .prompt().user(question).call().content();
        return content;
    }
}
