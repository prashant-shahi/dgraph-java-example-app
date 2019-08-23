package DgraphApp;

import com.google.common.collect.ImmutableMap;
import io.dgraph.DgraphClient;
import io.dgraph.DgraphGrpc;
import io.dgraph.DgraphGrpc.DgraphStub;
import io.dgraph.DgraphProto.Response;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Collections;
import java.util.Map;

public class Main {
    public static void main(final String[] args) {
        ManagedChannel channel =
            ManagedChannelBuilder.forAddress("localhost", 9080).usePlaintext(true).build();
        DgraphStub stub = DgraphGrpc.newStub(channel);
        DgraphClient dgraphClient = new DgraphClient(stub);

        String query = "query test($a: int, $b: int, $name: string) {\n  me(func: allofterms(name@en, $name)) {\n    name@en\n    director.film (first: $a, offset: $b) {\n      name @en\n      genre(first: $a) {\n        name@en\n      }\n    }\n  }\n}";

        Map<String, String> vars = ImmutableMap.of("$a", "5", "$b", "10", "$name", "Steven Spielberg");
        Response res = dgraphClient.newTransaction().queryWithVars(query, vars);
        System.out.println(res.getJson().toStringUtf8());
    }
}
