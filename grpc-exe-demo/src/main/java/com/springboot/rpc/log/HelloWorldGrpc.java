// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: helloworld.proto

package com.springboot.rpc.log;

public final class HelloWorldGrpc {
  private HelloWorldGrpc() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_springboot_rpc_log_HelloMessage_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_springboot_rpc_log_HelloMessage_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_springboot_rpc_log_HelloResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_springboot_rpc_log_HelloResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\020helloworld.proto\022\026com.springboot.rpc.l" +
      "og\"\037\n\014HelloMessage\022\017\n\007message\030\001 \001(\t\" \n\rH" +
      "elloResponse\022\017\n\007message\030\001 \001(\t2i\n\014HelloSe" +
      "rvice\022Y\n\010SayHello\022$.com.springboot.rpc.l" +
      "og.HelloMessage\032%.com.springboot.rpc.log" +
      ".HelloResponse\"\000B*\n\026com.springboot.rpc.l" +
      "ogB\016HelloWorldGrpcP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_com_springboot_rpc_log_HelloMessage_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_springboot_rpc_log_HelloMessage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_springboot_rpc_log_HelloMessage_descriptor,
        new java.lang.String[] { "Message", });
    internal_static_com_springboot_rpc_log_HelloResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_com_springboot_rpc_log_HelloResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_springboot_rpc_log_HelloResponse_descriptor,
        new java.lang.String[] { "Message", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
