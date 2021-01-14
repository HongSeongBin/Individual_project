import React, { Component } from "react";
import "antd/dist/antd.css";
import { Form, Input } from "antd";
import { withRouter } from "react-router-dom";

class MakeSurvey extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <h1 style={{ marginBottom: 20 }}>Title Part</h1>
        <Form.Item
          name="title"
          rules={[{ required: true, message: "Missing Title" }]}
          style={{ width: 500 }}
        >
          <Input placeholder="Title" />
        </Form.Item>
      </div>
    );
  }
}

export default withRouter(MakeSurvey);
