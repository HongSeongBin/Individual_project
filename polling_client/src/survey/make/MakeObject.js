import React, { Component } from "react";
import "antd/dist/antd.css";
import { Form, Input, Button, Space } from "antd";
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import { withRouter } from "react-router-dom";

class MakeObject extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <h1 style={{ marginBottom: 20 }}>Objective Question Part</h1>
        <Form.List name="objectives">
          {(fields, { add, remove }) => {
            return (
              <div>
                {fields.map((field) => (
                  <>
                    <Space
                      key={field.key}
                      style={{ display: "flex", marginBottom: 8 }}
                      align="start"
                    >
                      <Form.Item
                        {...field}
                        name={[field.name, "question"]}
                        fieldKey={[field.fieldKey, "question"]}
                        rules={[
                          { required: true, message: "Missing Question" },
                        ]}
                        style={{ width: 400 }}
                      >
                        <Input placeholder="Question" />
                      </Form.Item>

                      <MinusCircleOutlined
                        onClick={() => {
                          remove(field.name);
                        }}
                      />
                    </Space>
                    <Form.List name={[field.name, "answers"]}>
                      {(answers, { add, remove }) => {
                        return (
                          <div>
                            {answers.map((answer) => (
                              <Space key={answer.key} align="start">
                                <Form.Item
                                  {...answer}
                                  name={[answer.name, "answer"]}
                                  fieldKey={[answer.fieldKey, "answer"]}
                                  rules={[
                                    {
                                      required: true,
                                      message: "Missing answer",
                                    },
                                  ]}
                                >
                                  <Input placeholder="Answer" />
                                </Form.Item>

                                <MinusCircleOutlined
                                  onClick={() => {
                                    remove(answer.name);
                                  }}
                                />
                              </Space>
                            ))}

                            <Form.Item>
                              <div style={{ textAlign: "right" }}>
                                <Button
                                  type="dashed"
                                  onClick={() => {
                                    add();
                                  }}
                                  block
                                  style={{ width: 150, align: "right" }}
                                >
                                  <PlusOutlined /> Add answer
                                </Button>
                              </div>
                            </Form.Item>
                          </div>
                        );
                      }}
                    </Form.List>
                  </>
                ))}

                <Form.Item>
                  <Button
                    type="dashed"
                    onClick={() => {
                      add();
                    }}
                    block
                  >
                    <PlusOutlined /> Add Objective Survey Question
                  </Button>
                </Form.Item>
              </div>
            );
          }}
        </Form.List>
      </div>
    );
  }
}

export default withRouter(MakeObject);
