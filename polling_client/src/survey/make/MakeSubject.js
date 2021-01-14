import React, { Component } from "react";
import "antd/dist/antd.css";
import { Form, Input, Button, Space } from "antd";
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import { withRouter } from "react-router-dom";

class MakeSubject extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <h1 style={{ marginBottom: 20 }}>Subjective Question Part</h1>
        <Form.List name="subjectives">
          {(fields, { add, remove }) => (
            <>
              {fields.map((field) => (
                <Space
                  key={field.key}
                  style={{ display: "flex", marginBottom: 8 }}
                  align="baseline"
                >
                  <Form.Item
                    {...field}
                    name={[field.name, "question"]}
                    fieldKey={[field.fieldKey, "question"]}
                    rules={[{ required: true, message: "Missing Question" }]}
                    style={{ width: 500 }}
                  >
                    <Input placeholder="Question" />
                  </Form.Item>
                  <MinusCircleOutlined onClick={() => remove(field.name)} />
                </Space>
              ))}

              <Form.Item>
                <Button
                  type="dashed"
                  onClick={() => add()}
                  block
                  icon={<PlusOutlined />}
                >
                  Add Subjective Survey Question
                </Button>
              </Form.Item>
            </>
          )}
        </Form.List>
        <Form.Item>
          <div style={{ textAlign: "center" }}>
            <Button type="primary" htmlType="submit">
              Complete
            </Button>
          </div>
        </Form.Item>
      </div>
    );
  }
}

export default withRouter(MakeSubject);
