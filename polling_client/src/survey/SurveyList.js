import React, { Component } from "react";
import "antd/dist/antd.css";
import { Table, Spin, notification, Layout } from "antd";
import SurveyService from "../api/SurveyService";
import { Link, withRouter } from "react-router-dom";
import "./Loading.css";

const { Content, Footer } = Layout;

class SurveyList extends Component {
  constructor(props) {
    super(props);

    if (!this.props.isLogin) {
      this.props.history.push("/");
      notification.error({
        message: "Mini Polling",
        description: "Please Login first!",
      });
    }
  }

  state = {
    isLoading: true,
    surveys: [],
  };

  columns = [
    {
      title: "Number",
      dataIndex: "id",
      key: "id",
    },
    {
      title: "Title",
      dataIndex: "title",
      key: "title",
      render: (text) => (
        <Link
          to={{
            pathname: "/mainpage/vote",
            state: {
              surveyId: { text },
            },
          }}
        >
          {text}
        </Link>
      ),
    },
    {
      title: "Made by",
      dataIndex: "madeBy",
      key: "madeBy",
    },
    {
      title: "Date",
      dataIndex: "startDate",
      key: "startDate",
    },
  ];

  getSurveys = async () => {
    SurveyService.surveyList()
      .then((response) => {
        this.setState({ isLoading: false, surveys: response.data });
      })
      .catch((e) => {
        notification.error({
          message: "Mini Polling",
          description: "System error occur",
        });
      });
  };

  getMakingSurveys = async () => {
    SurveyService.madeList(this.props.userInfo)
      .then((response) => {
        this.setState({ isLoading: false, surveys: response.data });
      })
      .catch((e) => {
        notification.error({
          message: "Mini Polling",
          description: "System error occur",
        });
      });
  };

  getVotingSurveys = async () => {
    SurveyService.voteList(this.props.userInfo)
      .then((response) => {
        this.setState({ isLoading: false, surveys: response.data });
      })
      .catch((e) => {
        notification.error({
          message: "Mini Polling",
          description: "System error occur",
        });
      });
  };

  componentWillMount() {
    if (this.props.type == "All") this.getSurveys();
    else if (this.props.type == "Make") this.getMakingSurveys();
    else if (this.props.type == "Vote") this.getVotingSurveys();
  }

  render() {
    const { isLoading, surveys } = this.state;
    return (
      <Layout>
        {isLoading ? (
          <div class="Loading">
            <Spin />
          </div>
        ) : (
          <Content
            className="site-layout"
            style={{ padding: "0 50px", marginTop: 64 }}
          >
            <Table
              style={{ margin: "70px" }}
              columns={this.columns}
              dataSource={surveys}
            />
          </Content>
        )}

        <Footer style={{ textAlign: "center" }}>
          Mini Polling ©2021 Created by SungBin
        </Footer>
      </Layout>
    );
  }
}

export default withRouter(SurveyList);
