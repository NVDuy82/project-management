import React, {useEffect, useRef, useState} from "react";
import {Box, Menu} from "@mui/material";
import ReusableTable from "../components/ReusableTable";
import Scene from "./Scene";
import {isAuthenticated, logOut} from "../services/authenticationService";
import {useLoadUserInfo} from "../services/userService";
import {useNavigate} from "react-router-dom";
import {handleDate} from "../services/dateTimeService";
import {getMyNotifications} from "../services/notificationService";
import TitleTypography from "../components/TitleTypography";
import CustomMenuItem from "../components/CustumMenuItem";

const Notification = () => {
  // const [selectedRowId, setSelectedRowId] = useState(null); // State cho selected row ID
  const [selectedRow, setSelectedRow] = useState(null); // State cho selected row (dữ liệu của row)
  const [anchorElRow, setAnchorElRow] = useState(null); // State cho menu hành động

  const [data, setData] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [hasMore, setHasMore] = useState(false);

  const tableRef = useRef(null);
  const navigate = useNavigate();
  const loadInfo = useLoadUserInfo();

  const columns = [
    { id: "title", label: "Tiêu đề", filterable: false },
    { id: "body", label: "Nội dung", filterable: false },
    { id: "timestamp", label: "Thời gian", filterable: false, render: (row) => handleDate(row.timestamp) },
  ];

  useEffect(() => {
    loadInfo();
  }, []);

  useEffect(() => {
    if (!isAuthenticated()) {
      navigate("/login");
    } else {
      loadProjects(page);
    }
  }, [navigate, page]);

  const loadProjects = (page) => {
    setLoading(true);
    getMyNotifications(page)
        .then((response) => {
          setTotalPages(response.data.result.totalPages);
          setData((prevData) => [...prevData, ...response.data.result.data]);
          setHasMore(response.data.result.data.length > 0);
        })
        .catch((error) => {
          if (error.response?.status === 401) {
            logOut();
            navigate("/login");
          }
        })
        .finally(() => {
          setLoading(false);
        });
  }

  const handleScroll = () => {
    if (!hasMore) return;

    const tableContainer = tableRef.current;
    if (tableContainer) {
      const { scrollTop, scrollHeight, clientHeight } = tableContainer;
      if (scrollTop + clientHeight >= scrollHeight - 100) {
        if (page < totalPages) {
          setPage((prevPage) => prevPage + 1);
        }
        setHasMore(false);
      }
    }
  }

  // const handleClickRow = (e, rowId) => {
  //   setSelectedRowId(rowId); // Cập nhật selectedRowId khi click vào dòng
  // };

  const handleDoubleClickRow = (e, row) => {
    if (row.fromTaskId) {
      navigate("/task/" + row.fromTaskId);
    } else {
      navigate("/project/" + row.fromProjectId);
    }
    console.log(row);
  }

  const handleClickActionRow = (e, row) => {
    setAnchorElRow(e.currentTarget); // Mở menu hành động
    setSelectedRow(row); // Cập nhật selectedRow khi click vào hành động
  };

  const handleCloseMenuAction = () => {
    setAnchorElRow(null); // Đóng menu
  };

  const handleUpdate = () => {
    handleCloseMenuAction();
  };

  const handleRemove = () => {
    handleCloseMenuAction();
  };

  return (
    <Scene>
      <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 3 }}>
        <TitleTypography>
          Thông báo
        </TitleTypography>
      </Box>

      <ReusableTable
        columns={columns}
        data={data}
        onDoubleClickRow={handleDoubleClickRow}
        onActionClick={handleClickActionRow}
        loading={loading}
        onScroll={handleScroll}
        showAction={false}
        ref={tableRef}
      />

      {/* Menu for update and remove */}
      <Menu
        anchorEl={anchorElRow}
        open={Boolean(anchorElRow)}
        onClose={handleCloseMenuAction}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
        transformOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <CustomMenuItem onClick={handleUpdate}>Update</CustomMenuItem>
        <CustomMenuItem onClick={handleRemove}>Remove</CustomMenuItem>
      </Menu>
    </Scene>
  );
};

export default Notification;
