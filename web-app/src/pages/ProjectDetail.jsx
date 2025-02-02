import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {Box, Button, Typography} from "@mui/material";
import Scene from "./Scene";
import {styled} from "@mui/system";
import TitleTypography from "../components/TitleTypography";
import {getProjectById, leaveProject} from "../services/projectService";
import {useSnackbar} from "../context/SnackbarContext";
import PrevTitle from "../components/PrevTitle";
import IconButton from "@mui/material/IconButton";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import ButtonAddTitle from "../components/ButtonAddTitle";
import ExitToAppIcon from "@mui/icons-material/ExitToApp";
import ExitButton from "../components/ButtonExit";
import ConfirmDialog from "../components/ConfirmDialog";

const CustomButton = styled(Button)(({ theme }) => ({
  padding: '10px 20px',
  borderRadius: '8px',
  fontSize: '16px',
  fontWeight: 'bold',
  textTransform: 'none',
  boxShadow: '0px 4px 6px rgba(0, 0, 0, 0.1)',
  transition: 'all 0.3s ease',

  backgroundColor: '#332f2f',
  color: '#fff',
  '&:hover': {
    backgroundColor: '#777676',
  },
}));


const ProjectDetail = () => {
  const [confirmDialogOpen, setConfirmDialogOpen] = useState(false);

  const { showSnackbar } = useSnackbar();

  const { projectId } = useParams(); // Lấy projectId từ URL
  const navigate = useNavigate(); // Hook điều hướng của react-router-dom
  const [project, setProject] = useState({});

  useEffect(() => {
    getProjectById(projectId)
      .then((response) => setProject(response?.data?.result))
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  }, []);

  const projectName = project?.name;
  const description = project?.description;

  const handleLeaveProject = () => {
    leaveProject(project?.id)
      .then((response) => {
        showSnackbar("Rời dự án thành công", "success");

        navigate("/my-project")
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  };

  // Hàm điều hướng khi click vào button
  const handleNavigate = (path) => {
    navigate(`/project/${projectId}/${path}`);
  };

  return (
    <Scene>
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between", // Chia đều không gian giữa các thành phần
          alignItems: "center",
          mb: 3,
        }}
      >
        {/* Left Section: Title */}
        <Box sx={{ display: "flex", alignItems: "center" }}>
          <TitleTypography>
            {projectName}
          </TitleTypography>
        </Box>

        {/* Right Section */}
        <ExitButton onClick={() => setConfirmDialogOpen(true)}></ExitButton>
      </Box>


      <Box
        sx={{
          display: 'flex',
          gap: 2,
        }}
      >
        <CustomButton onClick={() => handleNavigate('member')}>Thành viên</CustomButton>
        <CustomButton onClick={() => handleNavigate('task')}>Công việc</CustomButton>
        <CustomButton onClick={() => handleNavigate('report')}>Thống kê</CustomButton>
      </Box>
      
      <Box sx={{ display: "flex", mt: 2 }}>
        <Typography
          sx={{
            textAlign: "left",
            fontSize: "16px",
            fontWeight: "normal",
            lineHeight: "1.5",
            color: "#4a4a4a",
            wordWrap: "break-word",
            fontFamily: "'Google Sans', sans-serif",
          }}
        >
          {description}
        </Typography>
      </Box>

      <ConfirmDialog
        open={confirmDialogOpen}
        onClose={() => setConfirmDialogOpen(false)}
        onConfirm={() => {
          handleLeaveProject();
          setConfirmDialogOpen(false);
        }}
        title="Xác nhận"
        message="Bạn có chắc chắn muốn rời dự án này?"
      />
    </Scene>
  );
}

export default ProjectDetail;