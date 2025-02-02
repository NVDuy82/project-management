import React, {useEffect, useRef, useState} from "react";
import {Box, Menu, MenuItem} from "@mui/material";
import ReusableTable from "../components/ReusableTable";
import {isAuthenticated, logOut} from "../services/authenticationService";
import {useLoadUserInfo} from "../services/userService";
import {useNavigate} from "react-router-dom";
import {getMyProjects} from "../services/projectService";
import {handleDate} from "../services/dateTimeService";

const MyProjectVersion2 = () => {
  const [selectedRowId, setSelectedRowId] = useState(null); // State cho selected row ID
  const [selectedRow, setSelectedRow] = useState(null); // State cho selected row (dữ liệu của row)
  const [anchorElRow, setAnchorElRow] = useState(null); // State cho menu hành động
  const [rawData, setRawData] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [hasMore, setHasMore] = useState(false);

  const tableRef = useRef(null);
  const navigate = useNavigate();
  const loadInfo = useLoadUserInfo();

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
    console.log("Load page : ", page);
    setLoading(true);
    getMyProjects(page)
      .then((response) => {
        setTotalPages(response.data.result.totalPages);
        setRawData((prevData) => [...prevData, ...response.data.result.data]);
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

  // Hàm xử lý sự kiện scroll
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
  };


  const columns = [
    { id: "name", label: "Tên", filterable: false },
    { id: "status", label: "Trạng thái", filterable: true },
    { id: "myRole", label: "Vai trò", filterable: true },
    { id: "createdAt", label: "Thời gian tạo", filterable: false },
  ];


  const formattedData = rawData.map(item => ({
    id: item.id,
    name: item.name,
    status: item.status.name,
    myRole: item.myRole.name,
    createdAt: handleDate(item.createdAt),
  }));

  const handleClickRow = (e, rowId) => {
    setSelectedRowId(rowId); // Cập nhật selectedRowId khi click vào dòng
  };

  const handleClickActionRow = (e, row) => {
    setAnchorElRow(e.currentTarget); // Mở menu hành động
    setSelectedRow(row); // Cập nhật selectedRow khi click vào hành động
  };

  const handleCloseMenuAction = () => {
    setAnchorElRow(null); // Đóng menu
  };

  const handleUpdate = () => {
    alert(`Updating row: ${selectedRow.name}`);
    handleCloseMenuAction();
  };

  const handleRemove = () => {
    alert(`Removing row: ${selectedRow.name}`);
    handleCloseMenuAction();
  };


  return (
    <Box>
      {/*<TableContainer*/}
      {/*  component={Paper}*/}
      {/*  ref={tableContainerRef}*/}
      {/*  style={{ maxHeight: 400, overflowY: 'auto' }} // Giới hạn chiều cao và cho phép cuộn dọc*/}
      {/*  onScroll={handleScroll}*/}
      {/*>*/}
      {/*  <Table stickyHeader>*/}
      {/*    <TableHead>*/}
      {/*      <TableRow>*/}
      {/*        <TableCell>ID</TableCell>*/}
      {/*        <TableCell>Tên</TableCell>*/}
      {/*        <TableCell>Mô tả</TableCell>*/}
      {/*      </TableRow>*/}
      {/*    </TableHead>*/}
      {/*    <TableBody>*/}
      {/*      {data.map(row => (*/}
      {/*        <TableRow key={row.id}>*/}
      {/*          <TableCell>{row.id}</TableCell>*/}
      {/*          <TableCell>{row.name}</TableCell>*/}
      {/*          <TableCell>{row.description}</TableCell>*/}
      {/*        </TableRow>*/}
      {/*      ))}*/}
      {/*    </TableBody>*/}
      {/*  </Table>*/}
      {/*</TableContainer>*/}

      <ReusableTable
        columns={columns}
        data={formattedData} // Hiển thị dữ liệu đã lọc
        selectedRowId={selectedRowId}
        onRowClick={handleClickRow}
        onActionClick={handleClickActionRow}
        ref={tableRef}
        onScroll={handleScroll}
      />

      {/* Menu for update and remove */}
      <Menu
        anchorEl={anchorElRow}
        open={Boolean(anchorElRow)}
        onClose={handleCloseMenuAction}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
        transformOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <MenuItem onClick={handleUpdate}>Update</MenuItem>
        <MenuItem onClick={handleRemove}>Remove</MenuItem>
      </Menu>
    </Box>
  );
};

export default MyProjectVersion2;
