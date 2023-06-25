package io.grpc.grpcswagger.openapi.v2;

import lombok.Data;

@Data
class ResponseObject {
    private String code;
    private ParameterSchema schema;
}
