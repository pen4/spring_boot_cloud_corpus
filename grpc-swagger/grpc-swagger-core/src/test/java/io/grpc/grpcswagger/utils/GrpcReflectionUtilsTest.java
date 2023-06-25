package io.grpc.grpcswagger.utils;

import org.junit.Test;

public class GrpcReflectionUtilsTest {

    @Test
    public void testParseToMethodDefinition() {
        System.out.println(GrpcReflectionUtils.parseToMethodDefinition("helloworld.Greeter.SayHello"));
    }
}
