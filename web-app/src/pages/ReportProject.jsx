import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {Box, Button, Card, Typography} from "@mui/material";
import Scene from "./Scene";
import {PieChart} from '@mui/x-charts/PieChart';
import IconButton from "@mui/material/IconButton";
import {styled} from "@mui/system";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import TitleTypography from "../components/TitleTypography";
import PrevTitle from "../components/PrevTitle";
import {getProjectById} from "../services/projectService";
import InfoLineReport from "../components/InfoLineReport";
import {getReport} from "../services/reportService";
import {useSnackbar} from "../context/SnackbarContext";

const CustomButton = styled(Button)(({ theme }) => ({
  padding: '0px 20px', // Loại bỏ padding để giống như văn bản
  borderRadius: 20, // Loại bỏ bo góc
  fontSize: '1.5rem', // Tương đương với font-size của Typography variant h5
  // fontWeight: 'bold', // Trọng lượng chữ như trong Typography
  textTransform: 'none', // Loại bỏ việc tự động chuyển thành chữ hoa
  backgroundColor: 'transparent', // Tắt màu nền
  color: "#5f6368", // Đảm bảo màu sắc giống văn bản
  boxShadow: 'none', // Không có bóng
  '&:hover': {
    backgroundColor: '#E7E8EB',
    color: "#5f6368",
  },
  '&:focus': {
    backgroundColor: '#E7E8EB',
    outline: 'none',
  }
}));


const ReportProject = () => {
  const { showSnackbar } = useSnackbar();
  const { projectId } = useParams(); // Lấy projectId từ URL
  const navigate = useNavigate(); // Hook điều hướng của react-router-dom
  const [projectName, setProjectName] = useState("");
  const [reportData, setReportData] = useState(
    {
      basic: {
        projectId: "",
        name: "",
        description: "",
        createdAt: "",
        updatedAt: "",
        status: {
          name: "",
          description: ""
        }
      },
      management: {
        managerIds: [""],
        createdBy: {
          id: "",
          userId: "",
          username: "",
          email: "",
          fullName: ""
        }
      },
      quantity: {
        managerCount: 0,
        membersCount: 0,
        viewerCount: 0,
        tasksCount: 0,
        todoCount: 0,
        inProgressCount: 0,
        completedCount: 0
      }
    }
  );

  useEffect(() => {
    getProjectById(projectId)
      .then((response) => setProjectName(response?.data?.result?.name))
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });

    getReport(projectId)
      .then((response) => {
        setReportData(response?.data?.result);
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  }, []);

  // const reportData = {
  //   basic: {
  //     projectId: "214edd9e-1c5c-4b4c-a378-6554dfbb6fed",
  //     name: "Project 0179",
  //     description: "Here is description for Project 0179",
  //     createdAt: "2024-12-08T19:46:49.355511Z",
  //     updatedAt: "2024-12-08T19:46:49.355511Z",
  //     status: {
  //       name: "ACTIVE",
  //       description: null
  //     }
  //   },
  //   management: {
  //     managerIds: ["1120bd3c-e4b3-4360-bc6b-f933fefe83b0"],
  //     createdBy: {
  //       id: "6755d6f864bfbc1c0e360471",
  //       userId: "1120bd3c-e4b3-4360-bc6b-f933fefe83b0",
  //       username: "user0023",
  //       email: "user0023@gmail.com",
  //       fullName: "Phạm Minh Hằng"
  //     }
  //   },
  //   quantity: {
  //     managerCount: 1,
  //     membersCount: 4,
  //     viewerCount: 2,
  //     tasksCount: 12,
  //     todoCount: 3,
  //     inProgressCount: 5,
  //     completedCount: 4
  //   }
  // };


  // Hàm điều hướng khi click vào button
  const handleNavigate = (path) => {
    navigate(`/project/${projectId}/${path}`);
  };

  return (
    <Scene>
      <Box sx={{ display: "flex", alignItems: "center", mb: 3 }}>
        <PrevTitle
          onClick={() => navigate(`/project/${projectId}`)} // Chuyển đến trang project
          sx={{ marginRight: 1 }} // Khoảng cách giữa button và typography
        >
          {projectName}
        </PrevTitle>
        <IconButton
          sx={{
            padding: 0,
            marginRight: 1,
          }}
        >
          <ChevronRightIcon />
        </IconButton>
        <TitleTypography>
          Thống kê
        </TitleTypography>
      </Box>

      <Box
        sx={{
          display: 'flex',
          gap: 2,
        }}
      >
        <Card sx={{ padding: 2, flex: 1 }}>
          <Typography variant="h6" color="black" mb={2} sx={{ fontFamily: "'Google Sans', sans-serif", }}>
            Thông tin dự án
          </Typography>
          <InfoLineReport label="Tên dự án" value={reportData.basic.name} allowEdit={false} />
          {/*<InfoLineReport label="Mô tả" value={reportData.basic.description || "Chưa có mô tả"} allowEdit={false} />*/}
          <InfoLineReport label="Trạng thái" value={reportData.basic.status.name} allowEdit={false} />
          <InfoLineReport label="Ngày tạo" value={new Date(reportData.basic.createdAt).toLocaleString()} allowEdit={false} />
          <InfoLineReport label="Ngày cập nhật" value={new Date(reportData.basic.updatedAt).toLocaleString()} allowEdit={false} />
        </Card>

        <Card sx={{ padding: 2, flex: 1 }}>
          <Typography variant="h6" color="black" mb={2} sx={{ fontFamily: "'Google Sans', sans-serif", }}>
            Thành viên
          </Typography>
          <InfoLineReport label="Quản lý dự án" value={reportData.management.createdBy.fullName} allowEdit={false} />
          <InfoLineReport label="Số người quản lý" value={reportData.quantity.managerCount} allowEdit={false} />
          <InfoLineReport label="Số thhành viên" value={reportData.quantity.membersCount} allowEdit={false} />
          <InfoLineReport label="Số người theo dõi" value={reportData.quantity.viewerCount} allowEdit={false} />

          <PieChart
            colors={['#5c5c5c', '#858585', '#bfbfbf']}
            series={[
              {
                data: [
                  { id: 0, value: reportData.quantity.managerCount, label: 'Quản lý' },
                  { id: 1, value: reportData.quantity.membersCount, label: 'Thành viên' },
                  { id: 2, value: reportData.quantity.viewerCount, label: 'Người theo dõi' },
                ],
                innerRadius: 30,
                outerRadius: 70,
                paddingAngle: 2,
                cornerRadius: 10,
                // startAngle: -45,
                // endAngle: 225,
                cx: 70,
                // cy: 150,
              },
            ]}
            height={200}
            legend={{
              labelStyle: {
                fontSize: '0.7rem',
                fontFamily: "'Google Sans', sans-serif",
              },
              position: { horizontal: 'right', vertical: 'middle' }, // Đặt vị trí của legend ở giữa ngang và trên cùng
            }}

          />
        </Card>

        <Card sx={{ padding: 2, flex: 1 }}>
          <Typography variant="h6" color="black" mb={2} sx={{ fontFamily: "'Google Sans', sans-serif", }}>
            Công việc
          </Typography>
          <InfoLineReport label="Số công việc" value={reportData.quantity.tasksCount} allowEdit={false} />
          <InfoLineReport label="Công việc chưa bắt đầu" value={reportData.quantity.todoCount} allowEdit={false} />
          <InfoLineReport label="Công việc đang làm" value={reportData.quantity.inProgressCount} allowEdit={false} />
          <InfoLineReport label="Công việc đã hoàn thành" value={reportData.quantity.completedCount} allowEdit={false} />


          <PieChart
            colors={['#d5ecd5', '#98FB98', '#0FFF50']}
            series={[
              {
                data: [
                  { id: 0, value: reportData.quantity.todoCount, label: 'Chưa bắt đầu' },
                  { id: 1, value: reportData.quantity.inProgressCount, label: 'Đang làm' },
                  { id: 2, value: reportData.quantity.completedCount, label: 'Đã hoàn thành' },
                ],
                innerRadius: 30,
                outerRadius: 70,
                paddingAngle: 2,
                cornerRadius: 10,
                // startAngle: -45,
                // endAngle: 225,
                cx: 70,
                // cy: 150,
              },
            ]}
            height={200}
            legend={{
              labelStyle: {
                fontSize: '0.7rem',
                fontFamily: "'Google Sans', sans-serif",
              },
              position: { horizontal: 'right', vertical: 'middle' }, // Đặt vị trí của legend ở giữa ngang và trên cùng
            }}

          />
        </Card>
      </Box>
    </Scene>
  );
}

export default ReportProject;