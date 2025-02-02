import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {Box, Button, Card, Typography} from "@mui/material";
import Scene from "./Scene";
import InfoLine from "../components/InfoLine";
import IconButton from "@mui/material/IconButton";
import {styled} from "@mui/system";
import TitleTypography from "../components/TitleTypography";
import {useSnackbar} from "../context/SnackbarContext";
import {assignTask, changeTaskStatus, deleteTask, getTaskById, unassignTask, updateTask} from "../services/taskService";
import PrevTitle from "../components/PrevTitle";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import InfoLineTaskDetail from "../components/InfoLineTaskDetail";
import InfoLineSelect from "../components/InfoLineSelect";
import {updateMyProfile} from "../services/userService";
import ConfirmDialog from "../components/ConfirmDialog";
import CustomMenuItem from "../components/CustumMenuItem";
import ExitButton from "../components/ButtonExit";
import {getProjectById} from "../services/projectService";
import ButtonAddTitle from "../components/ButtonAddTitle";
import ButtonRemove from "../components/ButtonRemove";
import ButtonUpdate from "../components/ButtonUpdate";
import AssignTaskDialog from "../components/AssignTaskDialog";
import ButtonRemoveRed from "../components/ButtonRemoveRed";

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


const TaskDetail = () => {
  const [isAssignTaskDialogOpen, setIsAssignTaskDialogOpen] = useState(false);
  const submitAssignTask = (data) => {
    assignTask(task?.id, data)
      .then((response) => {
        showSnackbar("Cập thật thành công", "success");
        setTask(response?.data?.result);
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  }

  const [confirmDialogOpen, setConfirmDialogOpen] = useState(false);
  const handleUnassignTask = () => {
    unassignTask(task?.id)
      .then((response) => {
        showSnackbar("Gở phụ trách thành công", "success");
        setTask(response?.data?.result);
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  };

  const [confirmDeleteDialogOpen, setConfirmDeleteDialogOpen] = useState(false);
  const handleDeleteTask = () => {
    deleteTask(task?.id)
      .then((response) => {
        showSnackbar("Đã xóa công việc", "success");

        if (task) {
          navigate(`/project/${task.projectId}/task`);
        } else {
          navigate(`/my-project`);
        }
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  };

  const { showSnackbar } = useSnackbar();

  const { taskId } = useParams(); // Lấy taskId từ URL
  const navigate = useNavigate();
  const [task, setTask] = useState({});
  const [project, setProject] = useState({});

  useEffect(() => {
    getTaskById(taskId)
      .then((response) => {
        setTask(response?.data?.result);
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  }, []);

  useEffect(() => {
    if (!task) return;
    if (!task?.projectId) return;

    getProjectById(task?.projectId)
      .then((response) => {
        setProject(response?.data?.result);
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  }, [task]);

  const updateTaskStatus = async (newStatus) => {
    try {
      const response = await changeTaskStatus(task.id, {
        status: newStatus,
      });
      setTask(response?.data?.result);
      showSnackbar("Cập nhật thành công", "success");

      return { success: true };
    } catch (error) {
      showSnackbar(error?.response?.data?.message, "error");

      return { success: false, error: error?.response?.data?.message };
    }
  }

  const updateName = async (newValue) => {
    try {
      const response = await updateTask(task.id, {
        name: newValue,
        description: task?.description,
      });
      setTask(response?.data?.result);
      showSnackbar("Cập nhật thành công", "success");

      return { success: true };
    } catch (error) {
      showSnackbar(error?.response?.data?.message, "error");

      return { success: false, error: error?.response?.data?.message };
    }
  }

  const updateDescription = async (newValue) => {
    try {
      const response = await updateTask(task.id, {
        name: task?.name,
        description: newValue,
      });
      setTask(response?.data?.result);
      showSnackbar("Cập nhật thành công", "success");

      return { success: true };
    } catch (error) {
      showSnackbar(error?.response?.data?.message, "error");

      return { success: false, error: error?.response?.data?.message };
    }
  }

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
          <PrevTitle
            onClick={() => {
              if (task) {
                navigate(`/project/${task.projectId}`)
              }
            }}
            sx={{ marginRight: 1 }}
          >
            {task?.projectName || "Project name"}
          </PrevTitle>
          <IconButton
            sx={{
              padding: 0,
              marginRight: 1,
            }}
          >
            <ChevronRightIcon />
          </IconButton>

          <PrevTitle
            onClick={() => {
              if (task) {
                navigate(`/project/${task.projectId}/task`)
              }
            }}
            sx={{ marginRight: 1 }}
          >
            {"Công việc"}
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
            {task?.name}
          </TitleTypography>
        </Box>

        {/* Right Section:  */}
        {project?.myRole?.name === "MANAGER" && task?.createdAt &&
          new Date() - new Date(task?.createdAt) < 3600000 && (
            <ButtonRemoveRed
              onClick={() => {
                setConfirmDeleteDialogOpen(true)
              }}
            >
            </ButtonRemoveRed>
          )
        }
      </Box>

      <Box
        sx={{
          display: "flex",
          gap: 2, // Khoảng cách giữa hai thẻ
          mt: 2,
        }}
      >
        <Card
          sx={{
            flex: 1,
            padding: 2,
            mb: 2,
            boxSizing: "border-box",
          }}
        >
          <Typography
            variant="h6"
            color="black"
            mb={2}
            sx={{ fontFamily: "'Google Sans', sans-serif" }}
          >
            Thông tin công việc
          </Typography>
          <InfoLine
            label="Tên công việc"
            value={task?.name || "Không xác định"}
            onSubmit={updateName}
          />
          <InfoLine
            label="Mô tả"
            value={task?.description || "Không có mô tả"}
            onSubmit={updateDescription}
          />
          <InfoLineSelect
            label="Trạng thái"
            value={task?.status?.name}
            options={[
              { value: "TODO", label: "Chưa bắt đầu" },
              { value: "IN_PROCESSING", label: "Đang thực hiện" },
              { value: "COMPLETED", label: "Hoàn thành" },
            ]}
            onSubmit={updateTaskStatus}
          />
          <InfoLine
            label="Ngày tạo"
            value={
              task?.createdAt
                ? new Date(task.createdAt).toLocaleString()
                : "Không xác định"
            }
            allowEdit={false}
          />
        </Card>

        <Card
          sx={{
            flex: 1,
            padding: 2,
            mb: 2,
            boxSizing: "border-box",
          }}
        >
          <Typography
            variant="h6"
            color="black"
            mb={2}
            sx={{ fontFamily: "'Google Sans', sans-serif" }}
          >
            Thông tin người phụ trách
          </Typography>
          <InfoLineTaskDetail
            label="Họ và tên"
            value={task?.assignedUserProfile?.fullName || "Không xác định"}
          />
          <InfoLineTaskDetail
            label="Email"
            value={task?.assignedUserProfile?.email || "Không xác định"}
          />
          <InfoLineTaskDetail
            label="Tên đăng nhập"
            value={task?.assignedUserProfile?.username || "Không xác định"}
          />
          {project?.myRole?.name === "MANAGER" && (
            <>
              <ButtonUpdate
                onClick={() => {
                  setIsAssignTaskDialogOpen(true);
                }}
                sx={{
                  marginTop: 2,
                  marginRight: 1,
                }}
              >
              </ButtonUpdate>
              <ButtonRemove
                onClick={() => {
                  setConfirmDialogOpen(true)
                }}
                sx={{
                  marginTop: 2,
                }}
              >
              </ButtonRemove>
            </>
          )}
        </Card>
      </Box>

      <AssignTaskDialog
        open={isAssignTaskDialogOpen}
        onClose={() => setIsAssignTaskDialogOpen(false)}
        projectId={project?.id}
        onSubmit={submitAssignTask}
      />

      <ConfirmDialog
        open={confirmDialogOpen}
        onClose={() => setConfirmDialogOpen(false)}
        onConfirm={() => {
          handleUnassignTask();
          setConfirmDialogOpen(false);
        }}
        title="Xác nhận"
        message="Bạn có chắc chắn muốn gỡ phân công phụ trách ở công việc này?"
      />

      <ConfirmDialog
        open={confirmDeleteDialogOpen}
        onClose={() => setConfirmDeleteDialogOpen(false)}
        onConfirm={() => {
          handleDeleteTask();
          setConfirmDeleteDialogOpen(false);
        }}
        title="Xác nhận"
        message="Bạn có chắc chắn muốn xóa công việc này?"
      />
    </Scene>
  );
}

export default TaskDetail;